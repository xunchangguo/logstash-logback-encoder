/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.logstash.logback.composite.loggingevent;

import java.io.IOException;

import net.logstash.logback.composite.AbstractFieldJsonProvider;
import net.logstash.logback.composite.FieldNamesAware;
import net.logstash.logback.composite.JsonWritingUtils;
import net.logstash.logback.fieldnames.LogstashFieldNames;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.fasterxml.jackson.core.JsonGenerator;

public class MessageJsonProvider extends AbstractFieldJsonProvider<ILoggingEvent> implements FieldNamesAware<LogstashFieldNames> {
    
    public static final String FIELD_MESSAGE = "message";

    public MessageJsonProvider() {
        setFieldName(FIELD_MESSAGE);
    }

    @Override
    public void writeTo(JsonGenerator generator, ILoggingEvent event) throws IOException {
        if(mayBeJSON(event.getFormattedMessage())) {
        	generator.writeFieldName(getFieldName()+ "_");
        	generator.writeRawValue(event.getFormattedMessage());
        } else {
            JsonWritingUtils.writeStringField(generator, getFieldName(), event.getFormattedMessage());
        }
    }
    
    public static boolean mayBeJSON( String string ) {
        return string != null
              && ("null".equals( string )
                    || (string.startsWith( "[" ) && string.endsWith( "]" )) || (string.startsWith( "{" ) && string.endsWith( "}" )));
     }
    
    @Override
    public void setFieldNames(LogstashFieldNames fieldNames) {
        setFieldName(fieldNames.getMessage());
    }

}
