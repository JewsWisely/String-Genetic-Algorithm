/*
   Name: Joel Anglister
   Date: 2/3/19
   
   A much more in-depth explanation of entire program can be found in the Generation class.
*/

public class Organism
{
   //alphabet contains every character on the keyboard
   static final String ALPHABET = " ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890,./;'[]\\-=!@#$%^&*()_+{}|\":?><";
   private char[] dna;
   private double fitness = 0;
   
   //target is the String that each Organism is trying to evolve into
   //Organism is constructed with dna and a fitness score
   //dna is an array of characters the same length as target and is generated randomly from ALPHABET
   //after dna is created, fitness of this particular Organism calculated
   public Organism(String target)
   {
      dna = new char[target.length()];
      for(int i = 0; i < target.length(); i++)
         dna[i] = ALPHABET.charAt((int)(Math.random() * ALPHABET.length()));
      fitness = getFitness(target);
   }
   
   //dna is not created randonmly, it is simply taken from the data String
   //fitness is still calculated
   public Organism(String data, String target)
   {
      dna = data.toCharArray();
      fitness = getFitness(target);
   }
   
   /*
      add up all of the shared characters in the correct indexes from this and the target
      example: 
         target = "top hat"
         dna = [p, o, t,  , 4, a, T]
         indexes 1, 3, and 5 are the same, so the fitness of this organism with respect to the target is 3 / 7 (as a percent)
   */
   private double getFitness(String target)
   {
      double fitness = 0;
      for(int i = 0; i < dna.length; i++)
         if(dna[i] == target.charAt(i))
            fitness++;
      return fitness / dna.length * 100;
   }
   
   //return fitness value
   public double getFitness()
   {
      return fitness;
   }
   
   //return dna array
   public char[] getDNA()
   {
      return dna;
   }
   
   //return String representation of dna array
   public String toString()
   {
      return new String(dna);
   }
}