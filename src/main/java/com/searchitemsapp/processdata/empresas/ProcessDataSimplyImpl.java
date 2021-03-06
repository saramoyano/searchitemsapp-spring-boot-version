package com.searchitemsapp.processdata.empresas;

import java.net.MalformedURLException;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.searchitemsapp.dto.UrlDTO;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ProcessDataSimplyImpl implements ProcessDataSimply {
	
	private static final String STRINGENIEMIN = "ñ";
	private static final String ENIEURL = "%F1";
	
	@Autowired
	private Environment env;

	@Override
	public List<String> getListaUrls(final Document document, final UrlDTO urlDto)
			throws MalformedURLException {
 
		String urlAux;
		int fin = 30;
		int max = 10;
		int incremento = 2;
		
		String urlBase = urlDto.getNomUrl();
		
		int numresultados = NumberUtils.toInt(env.getProperty("flow.value.paginacion.url.simply"));
		
		List<String> listaUrls = Lists.newArrayList();
		listaUrls.add(urlBase);
		
		for (int i = 2; i <= max; i++) {
			
			urlAux = urlBase.replace("=1&", "=".concat(String.valueOf(i).concat("&")));
			
			if(i == 2) {
				urlAux = urlAux.replace("&fin=10", "&fin=" + fin);
			} else {
				urlAux = urlAux.replace("&fin=10", "&fin=" + fin*incremento++);
			}
			
			listaUrls.add(urlAux);
		}
		
		if(numresultados > 0 && numresultados <= listaUrls.size()) {
			listaUrls = listaUrls.subList(0, numresultados);
		}
		
		return listaUrls;
	}

	public String reemplazarCaracteres(final String producto) {
		return producto.replace(STRINGENIEMIN, ENIEURL);
	}

	@Override
	public int get_DID() {
		return NumberUtils.toInt(env.getProperty("flow.value.did.empresa.simply"));
	}
}
