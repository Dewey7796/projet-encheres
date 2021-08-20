package org.Encheres.Servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Encheres.BusinessException;
import org.Encheres.bll.UtilisateurManager;
import org.Encheres.bo.Utilisateur;

/**
 * Servlet implementation class ServletMonProfil
 */
@WebServlet("/ServletMonProfil")
public class ServletMonProfil extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ServletMonProfil() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UtilisateurManager user = new UtilisateurManager();
		Utilisateur utilisateur = new Utilisateur();
		int currentUser = 16;
		try {
			utilisateur = user.selectByNoUtilisateur(currentUser);
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		request.setAttribute("pseudo", utilisateur.getPseudo());
		request.setAttribute("nom", utilisateur.getNom());
		request.setAttribute("prenom", utilisateur.getPrenom());
		request.setAttribute("email", utilisateur.getEmail());
		request.setAttribute("telephone", utilisateur.getTelephone());
		request.setAttribute("rue", utilisateur.getRue());
		request.setAttribute("code_postal", utilisateur.getCodePostal());
		request.setAttribute("ville", utilisateur.getVille());

		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/JSP/MonProfil.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
