import java.awt.Component;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WordEvolution
{
	private final static String match = "I came, I saw, I conquered -Julius Caesar";
	private final static double mutationRate = 0.01; //1.00 means 100% mutation rate.
	private final static int popSize = 5000;
	private static String[] population = new String[popSize];
	private static int[] fitness = new int[popSize];
	private static int generationCount = 1; 
	private static int fitnessLevel = 0;

	/*private static void hints()
	{
		for(int x=0; x<popSize; x++)
		{
			char[] pop = population[x].toCharArray();
			for(int y=0; y<match.length(); y++)
				if(match.charAt(y) == ' ')
					pop[y] = ' ';
			population[x] = new String(pop);
		}
	}*/
	private static void firstPopulation() //use ascii numbers between 32 and 122 inclusive. Creates the first population.
	{
		for(int x=0; x<popSize;x++)
		{
			String gene = ""; 
			for(int y=0; y<match.length(); y++)
				gene+=((char)((int)(Math.random()*91) + 32) + ""); 
			population[x] = gene; 
		}
	}
	private static void popFitness()
	{
		for(int x=0; x<popSize; x++)
		{
			double sameLetters = 0;
			for(int y=0; y<match.length(); y++)
				if(match.charAt(y) == population[x].charAt(y))
					sameLetters++;
			fitness[x] = (int)(sameLetters/match.length()*100.0);  
		}
	}
	private static ArrayList<String> popFitnessProb() //Used only in crossOver method 
	{
		ArrayList<String> matingPool = new ArrayList<String>();
		for(int x=0; x<popSize; x++)
			for(int y=0; y<fitness[x]; y++)
				matingPool.add(population[x]);
		System.out.println("Mating pool size: " + matingPool.size());
		return matingPool;
	}
	private static void crossOver() //genotype = phenotype 
	{
		ArrayList<String> matingPool = popFitnessProb();
		for(int x=0; x<popSize; x++)
		{
			String parent1 = matingPool.get((int)(Math.random()*matingPool.size())); //asexual reproduction is premitted; nonetheless, genetic diversity will be created. 
			String parent2 = matingPool.get((int)(Math.random()*matingPool.size()));
			String child = "";
			for(int y=0; y<match.length(); y++)
			{
				if(Math.random() < mutationRate)
			    	child += ((char)((int)(Math.random()*91) + 32) + "");
				else if(Math.random() < 0.5)
					child += (parent1.charAt(y) + "");
				else
					child += (parent2.charAt(y) + "");
			}
			population[x] = child; 
		}
		generationCount++;
	}
	private static boolean complete()
	{
		for(int fit : fitness)
			if(fit == 100)
				return true;
		return false;
	}
	private static String mostFit()
	{
		int max = 0;
		for(int x=1; x<popSize; x++)
			if(fitness[max] < fitness[x])
				max = x;
		fitnessLevel = fitness[max];
		return population[max];
	}
	private static void run() throws InterruptedException 
	{
		JFrame frame = new JFrame("Genetic Word");
		JPanel panel = new JPanel();
		
		firstPopulation();
		popFitness();
		JLabel word = new JLabel(mostFit());
		JLabel generation = new JLabel("Generation: " + generationCount);
		JLabel mutate = new JLabel("Mutation Rate: " + (mutationRate*100) + "%");
		JLabel initialPop = new JLabel("Initial Population: " + popSize);
		JLabel fitLevel = new JLabel("Fitness level (out of 100): " + fitnessLevel);
		
		word.setFont(new Font("Serif", Font.PLAIN, 60));
		fitLevel.setFont(new Font("Serif", Font.PLAIN, 50));
		generation.setFont(new Font("Serif", Font.PLAIN, 50));
		mutate.setFont(new Font("Serif", Font.PLAIN, 50));
		initialPop.setFont(new Font("Serif", Font.PLAIN, 50));
		
		word.setAlignmentX(Component.CENTER_ALIGNMENT);
		fitLevel.setAlignmentX(Component.CENTER_ALIGNMENT);
		generation.setAlignmentX(Component.CENTER_ALIGNMENT);
		mutate.setAlignmentX(Component.CENTER_ALIGNMENT);
		initialPop.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		
		panel.add(word);
		panel.add(fitLevel);
		panel.add(generation);
		panel.add(mutate);
		panel.add(initialPop);
	
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(2500, 450);
		frame.setLocationRelativeTo(null);
		
		frame.setVisible(true);
		
		
		do
		{
			 crossOver();
			 popFitness();
			 
			 word.setText(mostFit());
			 fitLevel.setText("Fitness level (out of 100): " + fitnessLevel);
			 generation.setText("Generation: " + generationCount);
	         panel.validate();
			 panel.repaint();
			 
			 Thread.sleep(50);
		}
		while(!complete());
	}
	public static void main(String[] args) throws InterruptedException
	{
		run();
		System.out.println("\n\nDem Noobs Evolved.");
	}
}



