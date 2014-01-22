package qa.hs.framework.data.def;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class TestConverter implements Converter {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean canConvert( Class type ) {
		return type.isAssignableFrom( TestRow.class );
	}

	@Override
	public void marshal( Object source, HierarchicalStreamWriter writer,	MarshallingContext context ) {
		TestRow test = (TestRow)source;
		for ( TestArguments arg : test.getArguments() ) {
			for ( ArgObject val : arg.getAllTestArguments() ) {
				writer.startNode( val.getKey() );
				writer.addAttribute( "type", val.getType() );
				writer.setValue(val.getVal());
				writer.endNode();
			}
		}
	}

	@Override
	public Object unmarshal( HierarchicalStreamReader reader, UnmarshallingContext context ) {
		TestArguments testargs = new TestArguments();
		while ( reader.hasMoreChildren() ) {
			reader.moveDown();
			String typeval = reader.getAttribute("type");
			if ( typeval.isEmpty() || typeval == "null" ) {
				typeval = "java.lang.String";
			}
			testargs.setArgument( new ArgObject( reader.getNodeName(), typeval, reader.getValue() ) );
			reader.moveUp();
		}
		TestRow result = new TestRow( testargs );
		return result;
	}

}
