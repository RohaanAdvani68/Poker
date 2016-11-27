package PokerGame;

public class Card
{
    String suit;
    String value;
    public Card(String s, String val)
    {
        suit=s;
        value=val;
    }
    public String getSuit()
    {
        return suit;
    }
    public int getValue()
    {
         if(value.equals("Ace"))
            return 14;
         else if(value.equals("Jack"))
            return 11;
         else if(value.equals("Queen"))
            return 12;
         else if(value.equals("King"))
            return 13;
         else
            return Integer.parseInt(value);
    }
    public void disp()
    {
        System.out.print(value+" "+suit+"\t");
    }
}
