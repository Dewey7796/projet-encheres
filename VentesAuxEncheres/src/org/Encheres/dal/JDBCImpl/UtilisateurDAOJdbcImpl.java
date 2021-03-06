package org.Encheres.dal.JDBCImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.Encheres.BusinessException;
import org.Encheres.bo.Utilisateur;
import org.Encheres.dal.DAO.DAOUtilisateur;
import org.Encheres.dal.JDBCTools.ConnectionProvider;

// commmentaires
public class UtilisateurDAOJdbcImpl implements DAOUtilisateur {

	public static final String INSERT_USER = "INSERT INTO UTILISATEURS (pseudo, nom, prenom, email, telephone, rue, code_postal, ville, mot_de_passe,credit, administrateur) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

	public static final String DELETE_USER = "DELETE FROM UTILISATEURS WHERE no_utilisateur = ?";

	public static final String UPDATE_USER = "UPDATE UTILISATEURS SET pseudo =?, nom=?, prenom=?, email=?, telephone=?, rue=?, code_postal=?, ville=?, mot_de_passe=? WHERE no_utilisateur =?";

	public static final String SELECT_USER = "SELECT * FROM UTILISATEURS WHERE no_utilisateur = ?";

	public static final String SELECT_USER_BY_PASSWORD = "SELECT * FROM UTILISATEURS WHERE pseudo = ? AND mot_de_passe = ?";

	public static final String SELECT_USER_BY_EMAIL = "SELECT email FROM UTILISATEURS WHERE email = ? ";

	public static final String UPDATE_PASSWORD = "UPDATE UTILISATEURS SET mot_de_passe =? WHERE email =?";

	@Override
	public void insert(Utilisateur data) throws BusinessException {

		if (data == null) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10000);
			throw businessException;
		}

		try (Connection cnx = ConnectionProvider.getConnection()) {
			cnx.setAutoCommit(false);
			PreparedStatement prstms = cnx.prepareStatement(INSERT_USER, PreparedStatement.RETURN_GENERATED_KEYS);

			prstms.setString(1, data.getPseudo());
			prstms.setString(2, data.getNom());
			prstms.setString(3, data.getPrenom());
			prstms.setString(4, data.getEmail());
			prstms.setString(5, data.getTelephone());
			prstms.setString(6, data.getRue());
			prstms.setString(7, data.getCodePostal());
			prstms.setString(8, data.getVille());
			prstms.setString(9, data.getMotDePasse());
			prstms.setInt(10, data.getCredit());
			if (data.getAdministrateur() == false) {
				prstms.setInt(11, 0);
			}

			prstms.executeUpdate();

			// if pour administrateur
			prstms.close();

			cnx.commit();
			cnx.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Erreur lors de la connection au serveur SQL");
		}
	}

	@Override
	public void update(Utilisateur data) throws BusinessException {

		try (Connection cnx = ConnectionProvider.getConnection()) {
			try {
				cnx.setAutoCommit(false);
				PreparedStatement prstms = cnx.prepareStatement(UPDATE_USER);
				prstms.setString(1, data.getPseudo());
				prstms.setString(2, data.getNom());
				prstms.setString(3, data.getPrenom());
				prstms.setString(4, data.getEmail());
				prstms.setString(5, data.getTelephone());
				prstms.setString(6, data.getRue());
				prstms.setString(7, data.getCodePostal());
				prstms.setString(8, data.getVille());
				prstms.setString(9, data.getMotDePasse());
				prstms.setInt(10, data.getNoUtilisateur());

				prstms.executeUpdate();
				prstms.close();
				cnx.commit();

				cnx.close();
			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10001);
			throw businessException;
		}

	}

	@Override
	public Utilisateur selectByNoUtilisateur(int noUtilisateur) throws BusinessException {

		Utilisateur user = new Utilisateur();

		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement prstms = cnx.prepareStatement(SELECT_USER);
			prstms.setInt(1, noUtilisateur);
			ResultSet rs = prstms.executeQuery();
			while (rs.next()) {
				if (noUtilisateur == rs.getInt("no_utilisateur")) {
					user = new Utilisateur(rs.getString("pseudo"), rs.getString("nom"), rs.getString("prenom"),
							rs.getString("email"), rs.getString("telephone"), rs.getString("rue"),
							rs.getString("code_postal"), rs.getString("ville"));
					user.setNoUtilisateur(noUtilisateur);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public void delete(int noUtilisateur) throws BusinessException {

		if (noUtilisateur < 0) {
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10003);
			throw businessException;
		}

		try (Connection cnx = ConnectionProvider.getConnection()) {
			try {
				cnx.setAutoCommit(false);
				// Ajout d'un article
				PreparedStatement prstms = cnx.prepareStatement(DELETE_USER);
				prstms.setInt(1, noUtilisateur);
				prstms.executeUpdate();

				prstms.close();
				cnx.commit();

				cnx.close();
			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10004);
			throw businessException;
		}

	}

	@Override
	public Utilisateur selectUtilisateurCourant(String login, String password) throws BusinessException {
		Utilisateur user = new Utilisateur();

		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement prstms = cnx.prepareStatement(SELECT_USER_BY_PASSWORD);
			prstms.setString(1, login);
			prstms.setString(2, password);
			ResultSet rs = prstms.executeQuery();
			while (rs.next()) {
				if (rs.getString("pseudo").equals(login) && rs.getString("mot_de_passe").equals(password)) {
					user = new Utilisateur(rs.getString("pseudo"), rs.getString("nom"), rs.getString("prenom"),
							rs.getString("email"), rs.getString("telephone"), rs.getString("rue"),
							rs.getString("code_postal"), rs.getString("ville"));
					if (rs.getByte("administrateur") == 1) {
						user.setAdministrateur(true);
					} else {

						user.setAdministrateur(false);
					}
					user.setCredit(rs.getInt("credit"));
					user.setNoUtilisateur(rs.getInt("no_utilisateur"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10004);
			throw businessException;
		}
		return user;
	}

	@Override
	public Utilisateur selectUtilisateurByEmail(String email) throws BusinessException {

		Utilisateur user = new Utilisateur();

		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement prstms = cnx.prepareStatement(SELECT_USER_BY_EMAIL);
			prstms.setString(1, email);
			ResultSet rs = prstms.executeQuery();
			while (rs.next()) {
				if (user.equals(null)) {
					user.setEmail("email@invalide.fr");
				}
				user.setEmail(email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10004);
			throw businessException;
		}
		return user;
	}

	@Override
	public Utilisateur updatePasswordByEmail(String motDePasse, String email) throws BusinessException {
		Utilisateur user = new Utilisateur();

		try (Connection cnx = ConnectionProvider.getConnection()) {
			try {
				cnx.setAutoCommit(false);
				PreparedStatement prstm = cnx.prepareStatement(SELECT_USER_BY_EMAIL);
				prstm.setString(1, email);
				ResultSet rs = prstm.executeQuery();
				while (rs.next()) {
					if (user.equals(null)) {
						user.setEmail("email@invalide.fr");
					}
					user.setEmail(email);
				}
				PreparedStatement prstms = cnx.prepareStatement(UPDATE_PASSWORD);
				prstms.setString(1, motDePasse);
				prstms.setString(2, email);

				prstms.executeUpdate();
				prstms.close();
				cnx.commit();

				cnx.close();
			} catch (Exception e) {
				e.printStackTrace();
				cnx.rollback();
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(10001);
			throw businessException;
		}
		return user;
	}

}
