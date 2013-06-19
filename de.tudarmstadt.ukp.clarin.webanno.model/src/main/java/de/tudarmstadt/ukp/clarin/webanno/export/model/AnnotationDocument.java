/*******************************************************************************
 * Copyright 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.tudarmstadt.ukp.clarin.webanno.export.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import de.tudarmstadt.ukp.clarin.webanno.model.AnnotationDocumentState;
/**
 * Annotation document information to be exported/imported
 * @author Seid Muhie Yimam
 *
 */
@JsonPropertyOrder(value = { "name", "user", "state" })
public class AnnotationDocument
{
    @JsonProperty("name")
    String name;
    @JsonProperty("user")
    String user;
    @JsonProperty("state")
    AnnotationDocumentState state;
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public String getUser()
    {
        return user;
    }
    public void setUser(String user)
    {
        this.user = user;
    }
    public AnnotationDocumentState getState()
    {
        return state;
    }
    public void setState(AnnotationDocumentState state)
    {
        this.state = state;
    }

}
