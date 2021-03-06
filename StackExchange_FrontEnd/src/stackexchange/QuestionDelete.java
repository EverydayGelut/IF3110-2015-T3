package stackexchange;

import models.Question;
import util.HttpRequest;
import util.XmlParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by elvan_owen on 11/18/15.
 */
@WebServlet(name = "QuestionDelete")
public class QuestionDelete extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HashMap<String, String> requestParams = new HashMap<>();
        ArrayList<String> params = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();

        requestParams.put("access_token", request.getParameter("token"));
        requestParams.put("id", request.getParameter("id"));
        requestParams.put("user_agent", request.getHeader("user-agent"));
        requestParams.put("ip_address", request.getRemoteAddr());

        // Set target method
        String requestResponse = HttpRequest.executeMethod("deleteQuestion", requestParams);

        // Get soap response
        String responseCode = XmlParser.checkResponse(requestResponse);
        String message = null;

        if (responseCode.equals("success")){
            response.setContentType("text/html;charset=UTF-8");
            response.sendRedirect("/?token=" + request.getParameter("token"));
//            request.getRequestDispatcher("/views/index.jsp").forward(request, response);
            return;
        } else if (responseCode.equals("expired")){
            response.sendRedirect("/login");
            return;
        }

        if (request.getParameter("token") != null && !request.getParameter("token").isEmpty()){
            response.setContentType("text/html;charset=UTF-8");

            request.setAttribute("error", "Only The Author can delete this question");
            request.getRequestDispatcher("/views/error.jsp").forward(request, response);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            request.getRequestDispatcher("/views/login.jsp").forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }
}
