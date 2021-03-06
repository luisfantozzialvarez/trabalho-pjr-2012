package br.edu.conversor.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import br.edu.conversor.entity.Vocabulo;

public class Converter {
	
	private static VocabuloDAO dao = new VocabuloDAO();
	private static List<Vocabulo> vocabulos = dao.Listar();
	private static List<String> palavrasAntigas = AntigasToArray();
	private static List<String> palavrasNovas = NovasToArray();
	
	private static List<String> AntigasToArray()
	{
		List<String> lista = new ArrayList<String>();
		for(Vocabulo vocabulo:vocabulos)
			lista.add(vocabulo.getVocabuloAntigo());
		return lista;
	}
	
	private static List<String> NovasToArray()
	{
		List<String> lista = new ArrayList<String>();
		for(Vocabulo vocabulo:vocabulos)
			lista.add(vocabulo.getVocabuloNovo());
		return lista;
	}
	
	private static String CaseCheck(String velha, String nova)
	{
		if(Character.isUpperCase(velha.charAt(0)))
			return new String( Character.toUpperCase(nova.charAt(0)) + nova.substring(1));
		else
			return nova;
	}
	
	public static String ConverterTexto(String texto)
	{
		
		String palavra ="";
		int indice = -1;
		for(char caractere : texto.toString().toCharArray() )
			if(Character.isLetter(caractere) || caractere =='-')
				palavra += caractere;
			else
			 if(palavra.trim().length()!=0)
			 {
				indice = palavrasAntigas.indexOf(palavra.toLowerCase());
				if (indice>=0)
					texto = texto.replace(palavra, CaseCheck(palavra,palavrasNovas.get(indice)));
				palavra = "";
			 }	
		indice = palavrasAntigas.indexOf(palavra.toLowerCase());
		if (indice>=0)
			texto = texto.replace(palavra,  CaseCheck(palavra,palavrasNovas.get(indice)));
		return texto;
	}
	
	public static void ConverterDOC(File file) throws Exception
	{
			FileInputStream fileinputstream = new FileInputStream(file);
			HWPFDocument hwpfdocument = new HWPFDocument(fileinputstream);
			WordExtractor wordextractor = new WordExtractor(hwpfdocument);
			
			String texto = wordextractor.getText();
			Range extensao = hwpfdocument.getRange();
			
			String palavra ="";
			int indice = -1;
			for(char caractere : texto.toString().toCharArray() )
				if(Character.isLetter(caractere) || caractere =='-')
					palavra += caractere;
				else
				 if(palavra.trim().length()!=0)
				 {
					indice = palavrasAntigas.indexOf(palavra.toLowerCase());
					if (indice>=0)
						extensao.replaceText(palavra, CaseCheck(palavra,palavrasNovas.get(indice)));
					palavra = "";
				 }	
			indice = palavrasAntigas.indexOf(palavra.toLowerCase());
			if (indice>=0)
				extensao.replaceText(palavra, CaseCheck(palavra,palavrasNovas.get(indice)));
		
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			hwpfdocument.write(fileoutputstream);
			
			fileinputstream.close();
			fileoutputstream.close();
			
	}
	public static void ConverterDOCX(File file) throws Exception
	{
			FileInputStream fileinputstream = new FileInputStream(file);
			XWPFDocument xwpfdocument = new XWPFDocument(fileinputstream);
			List<XWPFParagraph> paragrafos = xwpfdocument.getParagraphs();
			
			
			for (XWPFParagraph paragrafo: paragrafos)
			{
			
				
			String texto = paragrafo.getText();
			String palavra ="";
			
			for (XWPFRun run: paragrafo.getRuns())
				run.setText("",0);
		
			
			int indice = -1;
			for(char caractere : texto.toString().toCharArray() )
				if(Character.isLetter(caractere) || caractere =='-')
					palavra += caractere;
				else
				 if(palavra.trim().length()!=0)	
				 {
					indice = palavrasAntigas.indexOf(palavra.toLowerCase());
					if (indice>=0)
						texto =texto.replace(palavra, CaseCheck(palavra,palavrasNovas.get(indice)));
					palavra = "";
				 }	
			indice = palavrasAntigas.indexOf(palavra.toLowerCase());
			if (indice>=0)
				texto =texto.replace(palavra, CaseCheck(palavra,palavrasNovas.get(indice)));
			
			paragrafo.createRun().setText(texto);
			
			}
			
			FileOutputStream fileoutputstream = new FileOutputStream(file);
			xwpfdocument.write(fileoutputstream);
			
			fileinputstream.close();
			fileoutputstream.close();
			
	}
}
