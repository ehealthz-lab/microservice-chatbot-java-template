package main.java.resource;

import java.util.Set;

import org.alicebot.ab.AIMLProcessor;
import org.alicebot.ab.AIMLProcessorExtension;
import org.alicebot.ab.ParseState;
import org.alicebot.ab.Utilities;
import org.w3c.dom.Node;

public class Extension implements AIMLProcessorExtension {

    public Set<String> extensionTagNames = Utilities.stringSet("microservice");
    @Override
	public Set <String> extensionTagSet() {
        return extensionTagNames;
    }

	private String microservice(Node node, ParseState ps) {
        return AIMLProcessor.evalTagContent(node, ps, null);
    }
	
    @Override
	public String recursEval(Node node, ParseState ps) {
        try {
            String nodeName = node.getNodeName();
            if (nodeName.equals("microservice"))
            	return microservice(node, ps);
            else return (AIMLProcessor.genericXML(node, ps));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
}
