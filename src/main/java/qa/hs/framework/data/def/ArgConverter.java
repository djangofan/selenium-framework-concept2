package qa.hs.framework.data.def;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ArgConverter implements Converter {

        @SuppressWarnings("rawtypes")
		public boolean canConvert( Class clazz ) {
                return clazz.equals( ArgObject.class );
        }

		@Override
		public void marshal( Object source, HierarchicalStreamWriter writer, MarshallingContext context ) {
			ArgObject val = (ArgObject)source;
			writer.startNode( val.getKey() );
			writer.addAttribute( "type", val.getType() );
			writer.setValue( val.getVal() );			
			writer.endNode();
		}

		@Override
		public Object unmarshal( HierarchicalStreamReader reader, UnmarshallingContext context ) {
			ArgObject ao = new ArgObject();
			while ( reader.hasMoreChildren() ) {
		        reader.moveDown();	        
		        ao.setKey( reader.getNodeName() );
		        ao.setVal( reader.getValue() );
		        String typeval = reader.getAttribute("type"); // assuming never null, xml must contain type argument
                ao.setType( typeval );
		        reader.moveUp();
		    }
			return ao;
		}

}
	
