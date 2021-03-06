package com.searchitemsapp.processdata.empresas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.searchitemsapp.dto.UrlDTO;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ProcessDataCarrefourImpl implements ProcessDataEmpresas {
	
	private static final String PROTOCOL_ACCESSOR ="://";
	
	@Autowired
	private Environment env;

	@Override
	public List<String> getListaUrls(final Document document, 
			final UrlDTO urlDto) throws MalformedURLException {
		
		String urlBase = urlDto.getNomUrl();
		
		String selectorPaginacion = urlDto.getSelectores().getSelPaginacion();	
		
		int numresultados = NumberUtils.toInt(env.getProperty("flow.value.paginacion.url.carrefour"));
		
		StringTokenizer st = new StringTokenizer(selectorPaginacion,"|");  
		
		List<String> liSelectorAtr = Lists.newArrayList();
		
		while (st.hasMoreTokens()) {  
			liSelectorAtr.add(st.nextToken());
		}
			
		List<String> listaUrls = Lists.newArrayList();
		
		Elements elements = document.select(liSelectorAtr.get(0));
		
		listaUrls.add(urlBase);
		
		URL url = new URL(urlBase);
		String strUrlEmpresa = url.getProtocol()
				.concat(PROTOCOL_ACCESSOR).concat(url.getHost());
		
		for (Element element : elements) {
			listaUrls.add(strUrlEmpresa.concat(element.attr(liSelectorAtr.get(1))));
		}
		
		if(numresultados > 0 && numresultados <= listaUrls.size()) {
			listaUrls = listaUrls.subList(0, numresultados);
		}
		
		return listaUrls;
	}
	
	public int get_DID() {
		return NumberUtils.toInt(env.getProperty("flow.value.did.empresa.carrefour"));
	}
}
