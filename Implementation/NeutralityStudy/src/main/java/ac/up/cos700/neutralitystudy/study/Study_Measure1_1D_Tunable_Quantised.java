package ac.up.cos700.neutralitystudy.study;

import ac.up.cos700.neutralitystudy.experiment.Experiment_1D_Simple;
import ac.up.cos700.neutralitystudy.experiment.Experiment_1D_Tunable_Quantised;
import ac.up.cos700.neutralitystudy.neutralitymeasure.NeutralityMeasure1;
import ac.up.cos700.neutralitystudy.study.util.StudyConfigException;
import ac.up.malan.phd.problem.AlmostFlat;
import ac.up.malan.phd.problem.Flat;
import ac.up.malan.phd.problem.Griewank;
import ac.up.malan.phd.problem.Quartic;
import ac.up.malan.phd.problem.Rastrigin;
import ac.up.malan.phd.problem.Sine;
import ac.up.malan.phd.problem.Step;
import ac.up.malan.phd.problem.Table;
import ac.up.malan.phd.problem.TableLegs;

/**
 *
 * @author Abrie van Aardt
 */
public class Study_Measure1_1D_Tunable_Quantised extends Study {

    public Study_Measure1_1D_Tunable_Quantised() throws StudyConfigException {
        super();
        
        neutralityMeasure = new NeutralityMeasure1(config.entries.get("epsilon"));
        
        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Sine()));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Step(1)));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Table(1)));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new TableLegs()));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new AlmostFlat(1)));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Flat()));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Quartic(1)));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Griewank(1)));
//        experiments.add(new Experiment_1D_Tunable_Quantised(config, neutralityMeasure, new Rastrigin(1)));

    }
    
}
