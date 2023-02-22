/*
   Author: Joel Anglister
   Date: 2/3/19
   
   This program is uses a genetic algorithm in order to achieve its goal: to create a 
   target String. The reason it's called a genetic algorithm is because it is modeled
   after evolution; it tries to "evolve" until the goal is reached.
   
   Every genetic algorithm has a few key features: 
   
   DNA:
   This is the smallest unit of data, and in this case, our DNA is an array of
   characters that make up a String. I didn't need to make a DNA class since characters
   are a primitive data type, but other genetic algorithms may use objects as their
   smallest unit. 
   
   FITNESS:
   This comes from the concept of survival of the fittest. The fittest Organisms should
   have a higher chance of "reproducing" and passing their DNA to the next generation.
   For example, if the target String is "omglol" then "ololol" should be more likely to
   survive than "hahaha". Therefore, each Organism is given a fitness score based on how
   close it is to the target, and higher fitnesses yield higher chances of having similar
   words in the next generation. In this example, I score fitness as the percent of
   correct characters in the correct places. If the target is "wow" then "owo" gets 0%,
   "ooo" is 33.3%, "www" is 66.6%, and "wow" is 100%.
   
   HEREDITY:
   There must be a system where each new generation attains its information from "parents."
   The way I implemented this was with an ArrayList. Each Organism gets one entry into the 
   ArrayList for each fitness point it has. Thus, an Organism with 50% fitness is 5 times 
   more likely to be chosen than an Organism with 10% fitness. Once the ArrayList is full,
   two Organisms are chosen at random to be the parents.
   
   CROSSOVER:
   Once two parents are chosen for reproduction, a new baby Organism is created. It gets
   half of one parent's DNA and half of the other parent's DNA. Parents "Target is a store" 
   and "ok.JPG-??_!_a+photo" may produce baby "Tk.geG ?s_!_a shoro". Crossover is repeated
   until there is an entire new generation of Organisms with similar DNA to the previous 
   generation.
   
   VARIATION: 
   This is how we ensure a result will eventually be reached. Suppose the target is "rofl"
   and every new Organism was initialized with the DNA [@, 0, 2, y]. Every baby would always
   be exactly the same, as characters never switch indexes. Furthermore, only those 4
   characters would be used. For this reason, we initialize every Organism in the first
   generation with random characters. Futhermore, we use mutation in order to be extra sure
   that we have variety in our population. Every baby has a chance to become a completely new,
   random Organism with random DNA. So, if after 100 generations the current generation only 
   has Organisms with "jofl", mutation will ensure the possibility of an "r" appearing in the
   first index.
   
   OTHER COMMENTS:
   Since we know what the end goal of the genetic algorithm is, or the ideal solution, we know
   exactly when to stop. When the current generation contains an Organism with a fitness score
   of 100, we know we achieved our goal. However, in some genetic algorithms, the correct 
   answer is not already known, and therefore it won't be clear when to stop.
   
   Also, each of the concepts I just explained can be implemented in many different ways; it is
   up to the programmer. For example, I could have the Organisms reproduce asexually somehow, or
   I could have them reproduce by combining the DNA of 3 parents. Mutation could simply change
   a single character in a word. Crossover could be done by combining the first half od the 
   mother's DNA with the second half of the father's DNA. Parents could be chosen without the
   use of an ArrayList. Since this is my first genetic algorithm, I don't really have a personal
   preference, so I just did what I could think of.
   
   As it currently stands, the algorithm sometimes doesn't arrive at its target. Suppose a target
   "This String is 68 characters long, including spaces and punctuation.", a population size of
   1000, and a mutation rate of 0.01. Most of the population has the following phrase:
   "This String is 2H characters lon], includ_ng spaces and punctuation.". It is very close to
   the target phrase, but here is the problem: a mutated Organism may have some correct characters
   in the correct spaces, for example it has the characters 68 in the correct indexes, but since 
   the rest of the Organism does not resemble the target, it has a lower fitness, and is therefore
   much less likely to be chosen for reproduction. This results in stagnation in the population,
   sometimes not evolving a couple characters and as a result being stuck at 9X.X% fitness. (It
   actually can evolve the correct answer, it just takes a ridiculously long time. Evolving 109
   w's took 26,800 generations with a population size of 1000 and a mutation rate of 0.03. This
   is still faster than brute force, since there are 93 distinct characters in ALPHABET, the
   chance of randomly getting 109 w's is 1/93 ^ 109, or 1.763016 x 10^-203, around 2 in a thousand 
   decillion decillion decillion decillion decillion decillions.)
   
*/

import java.util.ArrayList;
import java.util.Scanner;

public class Generation
{
   //I only ever make one Generation object, so when I say generation, I refer to the array
   private Organism[] generation;
   private final String target;
   private final double mutationRate;
   static final Scanner scanner = new Scanner(System.in);
   
   //original generation has random Organisms with random DNA (characters)
   public Generation(int size, String target, double mutationRate)
   {
      this.target = target;
      this.mutationRate = mutationRate;
      generation = new Organism[size];
      for(int i = 0; i < generation.length; i++)
         generation[i] = new Organism(target);
   }
   
   //creates a double array of fitness scores of the Organisms
   private double[] getFitnessScores()
   {
      double[] fitnessScores = new double[generation.length];
      for(int i = 0; i < generation.length; i++)
         fitnessScores[i] = generation[i].getFitness();
      return fitnessScores;
   }
   
   //heredity, mutation, crossover happen here
   public void evolve()
   {
      //create array newGeneration to be filled with baby Organisms
      //create mating pool ArrayList to choose parents to mate, fill it as described above
      Organism[] newGeneration = new Organism[generation.length];
      ArrayList<Organism> matingPool = new ArrayList<Organism>();
      double[] fitnessScores = getFitnessScores();
      for(int i = 0; i < fitnessScores.length; i++)
         for(int k = 0; k < fitnessScores[i]; k++)
            matingPool.add(generation[i]);
      //for each empty spot in the new generation, do the following
      for(int i = 0; i < generation.length; i++)
      {
         if(Math.random() < mutationRate) //mutation, make completely random new Organism
            newGeneration[i] = new Organism(target);
         else if(matingPool.size() < 2) //NEW ADDITION
         {
            newGeneration[i] = matingPool.get((int)(Math.random() * 2));
         }
         else //crossover
         {
            //choose 2 different parents from mating pool, combine their DNA as described above to create a new Organism
            String father = matingPool.get((int)(Math.random() * matingPool.size())).toString(), mother = matingPool.get((int)(Math.random() * matingPool.size())).toString();
            String baby = "";
            for(int k = 0; k < father.length(); k++)
               baby += Math.random() < .5 ? father.charAt(k) : mother.charAt(k);
            newGeneration[i] = new Organism(baby, target);
         }
      }
      //newGeneration will almost always have better or equal DNA to the previous generation
      generation = newGeneration;
   }
   
   //return the fittest Organism (closest to target)
   public Organism getFittest()
   {
      double[] fitnessScores = getFitnessScores();
      int maxIndex = 0;
      for(int i = 0; i < generation.length; i++)
         if(fitnessScores[i] > fitnessScores[maxIndex])
            maxIndex = i;
      return generation[maxIndex];
   }
   
   //print the generation
   public String toString()
   {
      String str = "";
      for(int i = 0; i < generation.length; i++)
         str += generation[i].toString() + ", fitness = " + generation[i].getFitness() + "\n";
      return str;
   }
   
   public static void main(String[] args)
   {
      System.out.print("Target String: ");
      String target = scanner.nextLine();
      System.out.print("Population Size (~1000 recommended): ");
      int size = scanner.nextInt();
      System.out.print("Mutation Rate (between 0.01 and 0.05 recommended): ");
      double rate = scanner.nextDouble();
      Generation a = new Generation(size, target, rate);
      String max = "";
      int gen = 1;
      while(!max.equals(target))
      {
         max = a.getFittest().toString();
         System.out.println("generation: " + gen++ + "\t" + max + "\tfitness: " + a.getFittest().getFitness());
         a.evolve();
      }
   }
   
}