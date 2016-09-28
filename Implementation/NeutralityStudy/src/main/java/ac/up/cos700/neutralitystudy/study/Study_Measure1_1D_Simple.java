package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Experiment_1D_Simple;
import ac.up.cos700.neutralitystudy.function.problem.Quantiser;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.malan.phd.problem.Sine;
import ac.up.malan.phd.problem.Table;
import ac.up.malan.phd.problem.TableLegs;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_Measure1_1D_Simple extends Study {

    public Study_Measure1_1D_Simple() throws StudyConfigException {
        super();

        experiments.add(new Experiment_1D_Simple(config, new Table(1)));
        experiments.add(new Experiment_1D_Simple(config, new TableLegs()));
        

    }

}
