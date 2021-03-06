package org.Encheres.bll;

import java.time.LocalDate;

import org.Encheres.BusinessException;
import org.Encheres.bo.Article;
import org.Encheres.bo.Enchere;
import org.Encheres.dal.JDBCImpl.EnchereDAOJdbcImpl;

public class EnchereManager {

	private EnchereDAOJdbcImpl enchereDAO;

	public EnchereManager() {
		this.enchereDAO = new EnchereDAOJdbcImpl();
	}

	public void updateEnchere(Article article, int idutilisateur, int montantEnchere) throws BusinessException {
		BusinessException businessException = new BusinessException();

//		this.validerMontantEnchere(article.getPrixVente(), montantEnchere, businessException);
		if (!businessException.hasError()) {
			enchereDAO.update(article.getNoArticle(), idutilisateur, montantEnchere, LocalDate.now());
			article.setPrixVente(montantEnchere);
		} else {
			throw businessException;
		}
	}

	public void validerMontantEnchere(int montantInitEnchere, int montantEnchere, BusinessException exception) {
		if (montantInitEnchere > montantEnchere || montantInitEnchere == montantEnchere) {
			exception.ajouterErreur(CodesResultatBLL.ENCHERE_ERROR_AMOUNT);
		}

	}

	public void addEnchere(Enchere enchere) throws BusinessException {
		enchereDAO.insert(enchere);

	}

	public int selectAcheteur(int noArticle) throws BusinessException {
		return enchereDAO.selectBuyer(noArticle);

	}

	public void deleteEnchere(int noArticle) throws BusinessException {
		enchereDAO.delete(noArticle);
	}

}
