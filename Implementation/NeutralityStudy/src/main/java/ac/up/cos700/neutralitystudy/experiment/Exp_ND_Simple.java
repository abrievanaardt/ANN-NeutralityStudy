package ac.up.cos700.neutralitystudy.experiment;

import ac.up.cos700.neutralitystudy.data.Results;
import ac.up.cos700.neutralitystudy.function.problem.RealProblem;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure;
import ac.up.cos700.neutralitystudy.sampling.ProgressiveRandomWalkSampler;
import ac.up.cos700.neutralitystudy.study.util.StudyConfig;

/**
 *
 * @author Abrie van Aardt
 */
public class Exp_ND_Simple extends Experiment {

    public Exp_ND_Simple(StudyConfig _config, NeutralityMeasure _neutralityMeasure, RealProblem _problem) {
        super(_config, _neutralityMeasure, _problem);
        neutrality = new double[config.entries.get("simulations").intValue()];
        stepCount = config.entries.get("stepCount").intValue();
        stepRatio = config.entries.get("stepRatio");
        sampler = new ProgressiveRandomWalkSampler(problem, stepCount, stepRatio);
    }

    @Override
    protected void runSimulation(int currentSimulation) throws Exception {
        neutrality[currentSimulation - 1] = neutralityMeasure.measure(sampler.sample(), config.entries.get("epsilon"));
    }

    @Override
    protected void finalise() throws Exception {
        Results.writeToFile(path, name + "_Neutrality", neutrality);
    }

    protected final double[] neutrality;
    protected final int stepCount;
    protected final double stepRatio;

}
