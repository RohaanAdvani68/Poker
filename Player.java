package PokerGame;

public class Player
{
    String name;
    Card hand[];
    double balance;
    boolean playingRound;
    double AmountPlayedInRound;
    String password;
    public Player()
    {
        hand=new Card[2];
        name="";
        balance=0;
        playingRound=true;
        double AmountPlayedInRound=0;
    }
    public Player(String n,Card c1,Card c2,double b,String pword)
    {
        name=n;
        hand=new Card[2];
        hand[0]=c1;
        hand[1]=c2;
        balance=b;
        playingRound=true;
        double AmountPlayedInRound=0;
        password=pword;
    }
    public Player(String n,double b)
    {
        name=n;
        playingRound=true;
        hand=new Card[2];
        balance=b;
    }
    public Player(String n,String pword,double b)
    {
        name=n;
        hand=new Card[2];
        balance=b;
        playingRound=true;
        password=pword;
    }
    public void setHand(Card c1,Card c2)
    {
        hand[0]=c1;
        hand[1]=c2;
    }
    public void setBalance(double b)
    {
        balance=b;
    }
    public String getName()
    {
        return name;
    }
    public void displayHand()
    {
        hand[0].disp();
        hand[1].disp();
        System.out.println();
    }
    public double getBalance()
    {
        return balance;
    }
    public void dispBalance()
    {
        double ans=Math.round(balance*100.0)/100.0;
        System.out.println(name+": $"+ans);
    }
    public void play(double amount)
    {
        balance=balance-amount;
        AmountPlayedInRound+=amount;
    }
    public void fold()
    {
        playingRound=false;
    }
    public boolean isPlaying()
    {
        return playingRound;
    }
    public double AmountPlayed()
    {
        return AmountPlayedInRound;
    }
    public void ResetAmountPlayed()
    {
        AmountPlayedInRound=0;
    }
    public boolean passwordMatches(String pw)
    {
        if(pw.equals(password))
            return true;
        else
            return false;
    }
    public void Won(double pot)
    {
        balance+=pot;
    }
}
