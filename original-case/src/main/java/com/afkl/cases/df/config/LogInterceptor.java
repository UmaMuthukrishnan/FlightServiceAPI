package com.afkl.cases.df.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.afkl.cases.df.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
	@Value("${service.outputFile}")
	private String fileName;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long startTime = System.currentTimeMillis();
		log.info("\n-------- LogInterception.preHandle --- ");
		request.setAttribute("startTime", startTime);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		log.info("\n-------- LogInterception.postHandle --- ");
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("\n-------- LogInterception.afterCompletion --- ");

		long startTime = (Long) request.getAttribute("startTime") / 1000;
		long endTime = System.currentTimeMillis() / 1000;
		int status = response.getStatus();
		String url = request.getRequestURL().toString();
		createOutputFile(startTime, endTime, status, url);
	}

	public void fileSave(File file) {
		Scanner input = null;
		BufferedWriter out = null;
		try {
			input = new Scanner(file);
			while (input.hasNext()) {
				out = new BufferedWriter(new FileWriter(file));
				out.write(input.nextLine().replace("][", ","));
				out.flush();
			}
		}

		catch (IOException e) {
			log.error(e.getMessage());
		}

		finally {
			if (input != null) {
				input.close();
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					log.error(e.getMessage());
				}
			}

		}

	}

	public void createOutputFile(long startTime, long endTime, int status, String url) {
		File file = new File(fileName);
		ObjectMapper mapper = new ObjectMapper();
		try (FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true)) {
			ArrayNode arrayNode = mapper.createArrayNode();
			String responseTime = String.valueOf((endTime - startTime));
			String responseStatus = String.valueOf(status);
			if (file.exists()) {
				ObjectNode objectNode = mapper.valueToTree(new Response(url, responseTime, responseStatus));
				arrayNode.add(objectNode);
				mapper.writeValue(fileWriter, arrayNode);
				fileSave(file);
			} else {
				log.info(fileName + " file does not exists");
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

}
