package com.searchitemsapp.processdata;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.searchitemsapp.dao.UrlDao;
import com.searchitemsapp.dto.CategoriaDTO;
import com.searchitemsapp.dto.PaisDTO;
import com.searchitemsapp.dto.SelectoresCssDTO;
import com.searchitemsapp.dto.UrlDTO;
import com.searchitemsapp.processdata.empresas.lambdas.CondisEliminarTildes;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class UrlComposerImpl extends ProcessDataAbstract implements UrlComposer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UrlComposerImpl.class);   

	@Autowired
	private UrlDao ifUrlImpl;
	
	@Autowired
	private CategoriaDTO categoriaDto;
	
	@Autowired 
	private PaisDTO paisDto;

	public List<UrlDTO> replaceWildcardCharacter(final String strDidPais, 
			final String strDidCategoria, 
			final String strNomProducto,
			final String strEmpresas,
			final List<SelectoresCssDTO> listTodosSelectoresCss) 
			throws IOException {
		
		paisDto.setDid(NumberUtils.toInt(strDidPais));		
		categoriaDto.setDid(NumberUtils.toInt(strDidCategoria));
		
		List<UrlDTO> pListResultadoDto  = ifUrlImpl.obtenerUrlsPorIdEmpresa(paisDto, categoriaDto, strEmpresas);
		
		String productoTratadoAux = tratarProducto(strNomProducto);
		
		List<UrlDTO> listUrlDto = Lists.newArrayList();
	
		pListResultadoDto.forEach(urlDto -> {
			
			try {			
				
				cargaSelectoresCss(urlDto, listTodosSelectoresCss);
				
				String productoTratado = StringUtils.EMPTY;	
				
				if(urlDto.getBolActivo().booleanValue()) {
					if(getIFProcessDataEroski().get_DID() == urlDto.getDidEmpresa()) {
						productoTratado = getIFProcessDataEroski().reemplazarCaracteres(strNomProducto);
						productoTratado = tratarProducto(productoTratado);
					} else if(getIFProcessDataSimply().get_DID() == urlDto.getDidEmpresa()) {
						productoTratado = getIFProcessDataSimply().reemplazarCaracteres(strNomProducto);
						productoTratado = tratarProducto(productoTratado);
					} else if(getIFProcessDataCondis().get_DID() == urlDto.getDidEmpresa()) {
						
						CondisEliminarTildes eliminarTildes = (prd) -> {
							
							String[] arVocales = {"a","e","i","o","u"};
							String[] arTildes = {"á","é","í","ó","ú"};
							
							if(StringUtils.isAllEmpty(prd)) {
								return prd;
							}
							
							String productoAux = prd.toLowerCase();
							
							for (int i = 0; i < arVocales.length; i++) {
								productoAux = productoAux.replace(arTildes[i], 
										arVocales[i]);
							}
							
							return productoAux;
						};
						
						productoTratado = eliminarTildes.eliminarTildesProducto(strNomProducto);
						productoTratado = productoTratado.replace("ñ", "%D1");
						productoTratado = tratarProducto(productoTratado);
					} else {
						productoTratado = productoTratadoAux;
					}
					
					String urlAux = urlDto.getNomUrl();
					urlAux = urlAux.replace("{1}", productoTratado);
					urlDto.setNomUrl(urlAux);
					listUrlDto.add(urlDto);
					
				} else {
					if(LOGGER.isInfoEnabled()) {
						LOGGER.info("La URL: "
								.concat(urlDto.getDid().toString())
								.concat(" esta deshabilitada."));
					}
				}
			}catch(IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		
		return listUrlDto;
	}

	private void cargaSelectoresCss(UrlDTO urlDTO, List<SelectoresCssDTO> listTodosElementNodes) {
	
		SelectoresCssDTO selectoresCssDTO = listTodosElementNodes
				.stream().filter(x -> x.getDidEmpresa().equals(urlDTO.getDidEmpresa()))
				.collect(Collectors.toList()).get(0);
			
		urlDTO.setSelectores(selectoresCssDTO);
	}
}