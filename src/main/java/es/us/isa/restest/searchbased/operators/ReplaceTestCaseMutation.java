package es.us.isa.restest.searchbased.operators;

import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;

import es.us.isa.restest.searchbased.RestfulAPITestSuiteSolution;

public class ReplaceTestCaseMutation extends AbstractAPITestCaseMutationOperator{

	public ReplaceTestCaseMutation(double mutationProbability, PseudoRandomGenerator randomGenerator) {
		super(mutationProbability, randomGenerator);		
	}

	@Override
	protected void doMutation(double mutationProbability, RestfulAPITestSuiteSolution solution) {
		if(getRandomGenerator().nextDouble() <= mutationProbability) {
			int index= getRandomGenerator().nextInt(0, solution.getVariables().size()-1);
			solution.setVariable(index, solution.getProblem().createRandomTestCase());
		}
		
	}

}