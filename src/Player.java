import java.io.Serializable;

public class Player implements Serializable {
   private String name;
   private int score;
   private int pelliccions;
   private int cappottens;
   private int totalPlays;
   private int totalWins;

   Player(String name, int score, int pelliccions, int cappottens, int totalPlays, int totalWins) {
      this.name = name;
      this.score = score;
      this.pelliccions = pelliccions;
      this.cappottens = cappottens;
      this.totalPlays = totalPlays;
      this.totalWins = totalWins;
   }

   public int getCappottens() {
      return cappottens;
   }

   public int getPelliccions() {
      return pelliccions;
   }

   public int getScore() {
      return score;
   }

   public int getTotalPlays() {
      return totalPlays;
   }

   public int getTotalWins() {
      return totalWins;
   }

   public String getName() {
      return name;
   }

   public void setCappottens(int cappottens) {
      this.cappottens = cappottens;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setPelliccions(int pelliccions) {
      this.pelliccions = pelliccions;
   }

   public void setScore(int score) {
      this.score = score;
   }

   public void setTotalPlays(int totalPlays) {
      this.totalPlays = totalPlays;
   }

   public void setTotalWins(int totalWins) {
      this.totalWins = totalWins;
   }
}
