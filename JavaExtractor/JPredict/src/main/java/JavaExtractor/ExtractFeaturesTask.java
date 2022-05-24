package JavaExtractor;

import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import JavaExtractor.FeaturesEntities.ProgramFeatures;
import org.apache.commons.lang3.StringUtils;
import com.github.javaparser.JavaParser;
import com.github.javaparser.utils.StringEscapeUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

class ExtractFeaturesTask implements Callable<Void> {
    private final CommandLineValues m_CommandLineValues;
    private final String codeLine;

    public ExtractFeaturesTask(CommandLineValues commandLineValues, String codeLine) {
        m_CommandLineValues = commandLineValues;
        this.codeLine = codeLine;
    }

    @Override
    public Void call() {
        processFile();
        return null;
    }

    public void processFile() {
        ArrayList<ProgramFeatures> features;
        try {
            features = extractSingleFile();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if (features == null) {
            return;
        }

        String toPrint = featuresToString(features);
        if (toPrint.length() > 0) {
            System.out.println(toPrint);
        }
    }

    private ArrayList<ProgramFeatures> extractSingleFile() throws IOException {
        String code = codeLine.substring(2, codeLine.length() - 2); //Remove json object remnants from string
        String unescapedCode = StringEscapeUtils.unescapeJava(code); //Unescape characters so it parses to Java

        FeatureExtractor featureExtractor = new FeatureExtractor(m_CommandLineValues);

        return featureExtractor.extractFeatures(unescapedCode);
    }

    public String featuresToString(ArrayList<ProgramFeatures> features) {
        if (features == null || features.isEmpty()) {
            return Common.EmptyString;
        }

        List<String> methodsOutputs = new ArrayList<>();

        for (ProgramFeatures singleMethodFeatures : features) {
            StringBuilder builder = new StringBuilder();

            String toPrint = singleMethodFeatures.toString();
            if (m_CommandLineValues.PrettyPrint) {
                toPrint = toPrint.replace(" ", "\n\t");
            }
            builder.append(toPrint);


            methodsOutputs.add(builder.toString());

        }
        return StringUtils.join(methodsOutputs, "\n");
    }
}
