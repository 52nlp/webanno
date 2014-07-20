/*******************************************************************************
 * Copyright 2012
 * Ubiquitous Knowledge Processing (UKP) Lab and FG Language Technology
 * Technische Universität Darmstadt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.clarin.webanno.brat.controller;

import static org.apache.uima.fit.util.CasUtil.getType;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;

import de.tudarmstadt.ukp.clarin.webanno.api.AnnotationService;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationFeature;
import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationLayer;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;

/**
 * Utility Class for {@link TypeAdapter} with static methods such as geting
 * {@link TypeAdapter} based on its {@link CAS} {@link Type}
 *
 * @author Richard Eckart de Castilho
 * @author Seid Muhie Yimam
 *
 */
public final class TypeUtil {
	private TypeUtil() {
		// No instances
	}

	public static TypeAdapter getAdapter(AnnotationLayer aLayer,
			AnnotationService aAnnotationService) {
		if (aLayer.getType().equals(WebAnnoConst.SPAN_TYPE)) {
			TypeAdapter adapter = new SpanAdapter(aLayer);
			return adapter;
		} else if (aLayer.getType().equals(WebAnnoConst.RELATION_TYPE)) {
			TypeAdapter adapter = new ArcAdapter(aLayer.getId(),
					aLayer.getName(), "Dependent", "Governor",
					aLayer.getAttachFeature() == null ? null : aLayer
							.getAttachFeature().getName(), aLayer
							.getAttachType().getName());
			return adapter;
			// default is chain (based on operation, change to CoreferenceLinK)
		} else if (aLayer.getType().equals(WebAnnoConst.CHAIN_TYPE)) {
			ChainAdapter adapter = new ChainAdapter(aLayer.getId(),
					aLayer.getName() + ChainAdapter.CHAIN, aLayer.getName(), "first",
					"next");
			return adapter;

		}
		/*
		 * if (name.equals(AnnotationTypeConstant.POS)) { return
		 * SpanAdapter.getPosAdapter(); } else if
		 * (name.equals(AnnotationTypeConstant.LEMMA)) { return
		 * SpanAdapter.getLemmaAdapter(); } else if
		 * (name.equals(AnnotationTypeConstant.NAMEDENTITY)) { return
		 * SpanAdapter.getNamedEntityAdapter(); } else if
		 * (name.equals(AnnotationTypeConstant.DEPENDENCY)) { return
		 * ArcAdapter.getDependencyAdapter(); } else if
		 * (name.equals(AnnotationTypeConstant.COREFERENCE)) { return
		 * ChainAdapter.getCoreferenceChainAdapter(); } else if
		 * (name.equals(AnnotationTypeConstant.COREFRELTYPE)) { return
		 * ChainAdapter.getCoreferenceLinkAdapter(); }
		 */

		else {
			throw new IllegalArgumentException(
					"No adapter for type with name [" + aLayer.getName() + "]");
		}

	}

	/**
	 * Get the annotation type, using the request sent from brat. If the request
	 * have type POS_NN, the the annotation type is POS
	 *
	 * @param aType
	 *            the type sent from brat annotation as request while annotating
	 */
	public static String getLabelPrefix(String aType) {
		String annotationType;
		if (Character.isDigit(aType.charAt(0))) {
			annotationType = aType.substring(0, aType.indexOf("_") + 1)
					.replaceAll("[0-9]+", "");
		} else {
			annotationType = aType.substring(0, aType.indexOf("_") + 1);
		}
		return annotationType;
	}

	/**
	 * Get the annotation type the way it is used in Brat visualization page
	 * (PREFIX+Type), such as (POS_+NN)
	 */
	public static String getQualifiedLabel(AnnotationFeature aSelectedTag) {
		String annotationType = "";
		if (aSelectedTag.getLayer().getName()
				.equals(WebAnnoConst.POS)) {
			annotationType = WebAnnoConst.POS_PREFIX + aSelectedTag.getName();
		} else if (aSelectedTag.getLayer().getName()
				.equals(WebAnnoConst.DEPENDENCY)) {
			annotationType = WebAnnoConst.DEP_PREFIX + aSelectedTag.getName();
		} else if (aSelectedTag.getLayer().getName()
				.equals(WebAnnoConst.NAMEDENTITY)) {
			annotationType = WebAnnoConst.NAMEDENTITY_PREFIX
					+ aSelectedTag.getName();
		} else if (aSelectedTag.getLayer().getName()
				.equals(WebAnnoConst.COREFRELTYPE)) {
			annotationType = WebAnnoConst.COREFRELTYPE_PREFIX
					+ aSelectedTag.getName();
		} else if (aSelectedTag.getLayer().getName()
				.equals(WebAnnoConst.COREFERENCE)) {
			annotationType = WebAnnoConst.COREFERENCE_PREFIX
					+ aSelectedTag.getName();
		}
		return annotationType;
	}

	/**
	 * Get the annotation layer name for arc {@link AnnotationLayer} such as
	 * {@link WebAnnoConst#DEPENDENCY} or {@link WebAnnoConst#COREFERENCE} based
	 * on the origin span type. This is assumed that an arc is drawn only from
	 * single span type such as from {@link POS}. For Free Annotation type, the
	 * method should be changed.
	 */
	public static String getArcLayerName(String aPrefix) {
		String layer = "";
		if (aPrefix.equals(WebAnnoConst.POS_PREFIX)) {
			layer = WebAnnoConst.DEPENDENCY;
		} else if (aPrefix.equals(WebAnnoConst.COREFRELTYPE_PREFIX)) {
			layer = WebAnnoConst.COREFERENCE;
		}
		return layer;
	}

    /**
     * Construct the label text used in the brat user interface.
     */
    public static String getBratLabelText(TypeAdapter aAdapter, AnnotationFS aFs,
            List<AnnotationFeature> aFeatures)
    {
        StringBuilder bratLabelText = new StringBuilder();
        for (AnnotationFeature feature : aFeatures) {
            if (!(feature.isEnabled() || feature.isVisible())) {
                continue;
            }

            Feature labelFeature = aFs.getType().getFeatureByBaseName(feature.getName());

            if (bratLabelText.length() > 0) {
                bratLabelText.append(TypeAdapter.FEATURE_SEPARATOR);
            }

            bratLabelText.append(StringUtils.defaultString(aFs
                    .getFeatureValueAsString(labelFeature)));
        }
        
        if (bratLabelText.length() > 0) {
            return bratLabelText.toString();
        }
        else {
            // If there are no label features at all, then use the simple type name
            // FIXME: we use the CAS type name here - actually we should use the name configured in the
            // WebAnno layer configuration.
            Type type = getType(aFs.getCAS(), aAdapter.getAnnotationTypeName());
            return "(" + type.getShortName() + ")";
        }
    }
    
    public static String getBratTypeName(TypeAdapter aAdapter)
    {
        return aAdapter.getTypeId() + "_" + aAdapter.getAnnotationTypeName();
    }
    
    public static String getBratTypeName(AnnotationLayer aLayer)
    {
        return aLayer.getId() + "_" + aLayer.getName();
    }
}
