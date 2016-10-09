package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.data.Dataset;
import ac.up.cos700.neutralitystudy.data.util.IncorrectFileFormatException;
import ac.up.cos700.neutralitystudy.experiment.Exp_ND_Simple;
import ac.up.cos700.neutralitystudy.function.Identity;
import ac.up.cos700.neutralitystudy.function.Sigmoid;
import ac.up.cos700.neutralitystudy.function.problem.NetworkError;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.function.util.NotAFunctionException;
import ac.up.cos700.neutralitystudy.neuralnet.IFFNeuralNet;
import ac.up.cos700.neutralitystudy.neuralnet.metric.DefaultNetworkError;
import ac.up.cos700.neutralitystudy.neuralnet.util.FFNeuralNetBuilder;
import ac.up.cos700.neutralitystudy.neuralnet.util.ZeroNeuronException;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import java.io.FileNotFoundException;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_NN_Error_Simple extends Study_NN_Error {

    public Study_NN_Error_Simple() throws StudyConfigException {
        super();
    }

    @Override
    public Study setup(NeutralityMeasure nm) {
        super.setup(nm);

        for (RealProblem problem : problems) {
            experiments.add(new Exp_ND_Simple(config, neutralityMeasure, problem));
        }

        return this;
    }

}
