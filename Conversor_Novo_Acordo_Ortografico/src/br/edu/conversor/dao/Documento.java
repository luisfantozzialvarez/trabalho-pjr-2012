package br.edu.conversor.dao;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.model.UploadedFile;

public class Documento {
	
	File file;
	
	//Retorna Caminho da Aplica��o
	public String ServerContext()
	{
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();	
		FacesContext aFacesContext = FacesContext.getCurrentInstance();
		ServletContext context = (ServletContext) aFacesContext.getExternalContext().getContext();
		return context.getRealPath("/");
	}
	
	public Documento(UploadedFile arquivo) throws Exception 
	{		
			InputStream in = new BufferedInputStream(arquivo.getInputstream());
			
				for (int k=0;;k++)
					if(!new File(ServerContext()+"//WEB-INF//recebidos//"+k+"_"+arquivo.getFileName()).exists())
						{
							file = new File(ServerContext()+"//WEB-INF//recebidos//"+k+"_"+arquivo.getFileName());
							break;
						}
			
			FileOutputStream out = new FileOutputStream(file);  
			
			while (in.available() != 0) 
					out.write(in.read());  
			
			out.close();

			
	}
	
	public void Converter() throws Exception
	{
	    if(file.getName().endsWith(".txt")) 
        {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
	        StringBuffer bloco = new StringBuffer();
	         
	        while(true)
	        {
	           String textinLine = reader.readLine();
	            if(textinLine==null)
	                break;
	            bloco.append(textinLine);
	        }
			
	        bloco = ConverterBlocos.ConverterBloco(bloco);
	        
	        BufferedWriter saida = new BufferedWriter(new FileWriter(file));
	        saida.write(bloco.toString());
	        saida.close();
        }
        
	}
}