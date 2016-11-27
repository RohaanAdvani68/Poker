package PokerGame;
import java.util.*;

public class Poker
{
    static Random r=new Random();
    static Scanner sc=new Scanner(System.in);
    static  double smallBlind;
    static  double bigBlind;
    static Player players[]=new Player[10];;
    static int num_of_players;
    static double buyIn;
    static int dealer;
    boolean CardsUsed[][];
    Card CardsRevealed[];
    int num_of_cards_revealed;
    double pot;
    public Poker()
    {
        CardsUsed=new boolean[13][4];
        num_of_cards_revealed=0;
        CardsRevealed=new Card[5];
        pot=0;
    }
    public static void main()
    {
        System.out.println("Enter number of players:");
        num_of_players=sc.nextInt();
        System.out.println("Enter buy-in value:");
        buyIn=sc.nextDouble();
        System.out.println("Enter value of blinds:");
        smallBlind=sc.nextDouble();
        bigBlind=sc.nextDouble();
        for(int i=0;i<num_of_players;i++)
        {
            System.out.println("Enter name of player " + (i+1));
            String name=sc.next();
            System.out.println("Enter your password");
            String pword=sc.next();
            System.out.print('\u000C');//to clear screen
            players[i]=new Player(name,pword,buyIn);
        }
        Poker g=new Poker();
        g.SelectDealer();
        boolean playing=true;
        while(playing)
        {
            Poker game=new Poker();
            game.Start();
            System.out.println("Press enter to continue the game or type \"stop\" to end the game and see overall balances");
            String s=sc.next();
            if(s.equalsIgnoreCase("stop"))
            {
                playing=false;
            }
            dealer=(dealer+1)%num_of_players;
        }
        System.out.println("FINAL BALANCES");
        for(int i=0;i<num_of_players;i++)
        {
            players[i].setBalance(players[i].getBalance()-buyIn);
            players[i].dispBalance();
        }
    }
    
    public void Start()
    {
        SetHands();
        ShowHands();
        Menu();
    }
    public void Menu()
    {
        System.out.println("1- Start betting \n2- Show a player's hand \n3- Show everyone's stack \n4- Reveal next cards\n5- Show Pot\n6- End this deal");
        int num=sc.nextInt();
        switch(num)
        {
            case 1: 
                Betting();
                break;
            case 2:
                System.out.println("Who wants to check their hand?");
                boolean b=true;
                while(b)
                {
                    String s=sc.next();
                    Player p=FindPlayer(s);
                    if(p==null)
                    {
                        System.out.println("No player found with that name. Please re-enter name.");
                        continue;
                    }
                    else
                    {
                        ShowPlayerHand(p);
                        b=false;
                    }
                }
                break;
            case 3:
                for(int i=0;i<num_of_players;i++)
                {
                    players[i].dispBalance();
                }
                break;
            case 4:
                if( num_of_cards_revealed==0)
                    Flop();
                else if(num_of_cards_revealed==3)
                    Turn();
                else if(num_of_cards_revealed==4)
                    River();
                else    
                    System.out.println("All cards have been revealed");
                break;
            case 5:
                System.out.println("Pot: "+pot);
                break;
            case 6:
                Player p=null;
                while(p==null)
                {
                    System.out.println("Who won this deal?");
                    String winner=sc.next();
                    p=FindPlayer(winner);
                    if(p!=null)
                    {
                        p.Won(pot);
                    }
                    else
                        System.out.println("Could not find any player with that name. Please re-enter name.");
                }
                return;
            default:
                Menu();
        }
        Menu();
    }
    public Player FindPlayer(String s)
    {
            for(int i=0;i<num_of_players; i++)
            {
                if(s.equalsIgnoreCase(players[i].getName()))
                {
                    return players[i];
                }
            }
            return null;
    }
    public void ShowHands()
    {
        for(int i=(dealer+1)%num_of_players;i!=dealer;i=(i+1)%num_of_players)
        {
            ShowPlayerHand(players[i]);
        }
        ShowPlayerHand(players[dealer]);
    }
    public void ShowPlayerHand(Player p)
    {   
        System.out.println("Please pass the laptop to "+p.getName()+"\nPress enter when ready to see your hand");
        String s=sc.next();
        System.out.println("Enter password");
        String pw=sc.next();
        if(p.passwordMatches(pw))
        {
            p.displayHand();
            System.out.println("Press enter when you are done");
            s=sc.next();
            System.out.print('\u000C');
        }
    }
    public void Betting()
    {
        int action=(dealer+1)%num_of_players;
        double toPlay;
        double current_bet=0;
        boolean b=true;
        if(num_of_cards_revealed==0)//pre-flop
        {
            Player Small=players[action];
            Small.play(smallBlind);
            System.out.println(Small.getName()+" is small blind");
            action=(action+1)%num_of_players;
            Player Big=players[action];
            Big.play(bigBlind);
            System.out.println(Big.getName()+" is big blind");
            pot+=(smallBlind+bigBlind);
            current_bet=bigBlind;
            action=(action+1)%num_of_players;//action starts at person after big
            do
            {
                if(players[action].isPlaying()==false)
                    continue;
                System.out.println("Action on "+players[action].getName());
                System.out.println("Press C to call, R to raise, F to fold, Z to end round of betting");
                char c=(sc.next().toLowerCase()).charAt(0);
                switch(c)
                {
                    case 'c':
                        toPlay=current_bet-players[action].AmountPlayed();
                        players[action].play(toPlay);
                        pot+=toPlay;
                        break;
                    case 'r':
                        System.out.println("Enter bet");
                        current_bet=sc.nextDouble();
                        toPlay=current_bet-players[action].AmountPlayed();
                        players[action].play(toPlay);
                        pot+=toPlay;
                        break;
                    case 'f':
                        players[action].fold();
                        break;
                    case 'z':
                        b=false;
                        break;
                }
                action=(action+1)%num_of_players;
            }while(b);
        }
        else
        {
            do{
                if(players[action].isPlaying()==false)
                {
                    action=(action+1)%num_of_players ;
                    continue;
                }
                System.out.println("Action on "+players[action].getName());
                if(current_bet==0)
                {
                    System.out.println("Press C to check, R to raise, F to fold, Z to end round of betting");
                    char c=(sc.next().toLowerCase()).charAt(0);
                    switch(c)
                    {
                        case 'c':
                        System.out.println("CHECK");
                        break;
                        case 'r':
                        System.out.println("Enter bet");
                        current_bet=sc.nextDouble();
                        players[action].play(current_bet);
                        pot+=current_bet;
                        break;
                        case 'f':
                        players[action].fold();
                        break;
                        case 'z':
                        b=false;
                        break;
                    }
                }
                else//if bet is currently not zero
                {
                    System.out.println(current_bet+" to play");
                    System.out.println("Press C to call, R to raise, F to fold, Z to end round of betting");
                    char c=(sc.next().toLowerCase()).charAt(0);
                    switch(c)
                    {
                        case 'c':
                        toPlay=current_bet-players[action].AmountPlayed();
                        players[action].play(toPlay);
                        pot+=toPlay;
                        break;
                        
                        case 'r':
                        System.out.println("Enter bet");
                        current_bet=sc.nextDouble();
                        toPlay=current_bet-players[action].AmountPlayed();
                        players[action].play(toPlay);
                        pot+=toPlay;
                        break;
                        
                        case 'f':
                        players[action].fold();
                        break;
                        
                        case 'z':
                        b=false;
                        break;
                    }
                }
                
                action=(action+1)%num_of_players ;
            }while(b);
            
            //to set all players AmountPlayed to 0 after the round of betting
            for(int i=0;i<num_of_players;i++)
            {
                if(players[i].isPlaying())
                    players[i].ResetAmountPlayed();
            }
        }
    }
    public void Flop()
    {
        Card c1=GenerateCard();
        Card c2=GenerateCard();
        Card c3=GenerateCard();
        c1.disp();
        c2.disp();
        c3.disp();
        System.out.println();
        num_of_cards_revealed+=3;
        CardsRevealed[0]=c1;
        CardsRevealed[1]=c2;
        CardsRevealed[2]=c3;
    }
    public void Turn()
    {
        Card c1=GenerateCard();
        CardsRevealed[3]=c1;
        num_of_cards_revealed++;
        for(int i=0;i<num_of_cards_revealed;i++)
            CardsRevealed[i].disp();
        System.out.println();
    }
    public void River()
    {
        Card c1=GenerateCard();
        CardsRevealed[4]=c1;
        num_of_cards_revealed++;
        for(int i=0;i<num_of_cards_revealed;i++)
            CardsRevealed[i].disp();
        System.out.println();
    }
    public void SelectDealer()
    {
        System.out.println("To select the dealer");
        int max=0;
        int max_pos=0;
        for(int i=0;i<num_of_players;i++)
        {
            Card c=GenerateCard();
            System.out.println(players[i].getName());
            c.disp();
            System.out.println();
            if (c.getValue()>max)
            {
                max=c.getValue();
                max_pos=i;
            }
        }
        dealer=max_pos;
        System.out.println(players[dealer].getName()+" is the dealer");
    }
    public void SetHands()
    {
        for(int i=(dealer+1)%num_of_players;i!=dealer;i=(i+1)%num_of_players)
        {
            players[i].setHand(GenerateCard(), GenerateCard());
        }
        players[dealer].setHand(GenerateCard(), GenerateCard());
    }
    
    public Card GenerateCard()
    {
        int SuitNum=r.nextInt(4);
        int Value=r.nextInt(13);
        while(CardsUsed[Value][SuitNum]==true)//if card has been used 
        {
            SuitNum=(int)(Math.random()*4);
            Value=(int)(Math.random()*13);
        }
        String suit="";
        switch(SuitNum)
        {
            case 0:
                suit="Hearts";
                break;
            case 1:
                suit="Clubs";
                break;
            case 2:
                suit="Spades";
                break;
            case 3:
                suit="Diamonds";
                break;
        }
        String val="";
        switch(Value+1)
        {
            case 1:
                val="Ace";
                break;
            case 11:
                val="Jack";
                break;
            case 12:
                val="Queen";
                break;
            case 13:
                val="King";
                break;
            default:
                val=Integer.toString(Value+1);
                break;
        }
        Card c=new Card(suit,val);
        CardsUsed[Value][SuitNum]=true;
        return c;
    }
}
