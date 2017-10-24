package pkgCore;

import java.util.ArrayList;
import java.util.Collections;

import pkgEnum.eCardNo;
import pkgEnum.eHandStrength;
import pkgEnum.eRank;
import pkgEnum.eSuit;

public class HandPoker extends Hand {
	
	private ArrayList<CardRankCount> CRC;
	HandScorePoker HSP = (HandScorePoker)this.getHS();
	
	protected ArrayList<CardRankCount> getCRC() {
		return CRC;
	}
	
	public HandPoker() {
		this.setHS(new HandScorePoker());
		this.HSP = (HandScorePoker) this.getHS();
	}

	@Override
	public HandScore ScoreHand() {
		Collections.sort(super.getCards());
		Frequency();
		
		if (isRoyalFlush()) {
			return HSP;
		} else if (isStraightFlush()) {
			return HSP;
		} else if (isFourOfAKind()) {
			return HSP;
		} else if (isFullHouse()) {
			return HSP;
		} else if(isFlush()) {
			return HSP;
		} else if(isStraight()) {
			return HSP;
		} else if(isThreeOfAKind()) {
			return HSP;
		} else if(isTwoPair()) {
			return HSP;
		} else if(isPair()) {
			return HSP;
		} else {
			isHighCard();
			return HSP;
		}

	}
	private void Frequency() {

		CRC = new ArrayList<CardRankCount>();

		int iCnt = 0;
		int iPos = 0;

		for (eRank eRank : eRank.values()) {
			iCnt = (CountRank(eRank));
			if (iCnt > 0) {
				iPos = FindCardRank(eRank);
				CRC.add(new CardRankCount(eRank, iCnt, iPos));
			}
		}
		
		Collections.sort(CRC);
	}

	private int CountRank(eRank eRank) {
		int iCnt = 0;
		for (Card c : super.getCards()) {
			if (c.geteRank() == eRank) {
				iCnt++;
			}
		}
		return iCnt;
	}

	private int FindCardRank(eRank eRank) {
		int iPos = 0;

		for (iPos = 0; iPos < super.getCards().size(); iPos++) {
			if (super.getCards().get(iPos).geteRank() == eRank) {
				break;
			}
		}
		return iPos;
	}
	
	private ArrayList<Card> FindTheKickers(ArrayList<CardRankCount> CRC)
	{
		ArrayList<Card> kickers = new ArrayList<Card>();
		
		for (CardRankCount crcCheck: CRC)
		{
			if (crcCheck.getiCnt() == 1)
			{
				kickers.add(this.getCards().get(crcCheck.getiCardPosition()));
			}
		}
		
		return kickers;
	}
	
	// Found hand scoring methods 
	
	public boolean isRoyalFlush() {
		boolean bIsRoyalFlush = false;

		if (isFlush() && isStraight() &&
				super.getCards().get(eCardNo.FIRST.getiCardNo()).geteRank() == eRank.ACE
				&& super.getCards().get(eCardNo.SECOND.getiCardNo()).geteRank() == eRank.KING) {
			bIsRoyalFlush = true;
			
			HSP.seteHandStrength(eHandStrength.RoyalFlush);
			HSP.setHiCard(null);
			HSP.setLoCard(null);
			HSP.setKickers(null);
			
			this.setHS(HSP);
		}
		
		return bIsRoyalFlush;
	}

	public boolean isStraightFlush() {
		boolean bisStraightFlush = false;

		if (isFlush() && isStraight()) {
			bisStraightFlush = true;
			
			HSP.seteHandStrength(eHandStrength.StraightFlush);
			HSP.setHiCard(super.getCards().get(eCardNo.FIRST.getiCardNo()));
			HSP.setLoCard(null);
			HSP.setKickers(null);
			
			this.setHS(HSP);
		}
		
		return bisStraightFlush;
	}

	public boolean isFourOfAKind() {
		boolean bisFourOfAKind = false;
		
		if (CRC.get(0).getiCnt() == 4) {
			bisFourOfAKind = true;
			
			HSP.seteHandStrength(eHandStrength.FourOfAKind);
			HSP.setHiCard(super.getCards().get(CRC.get(0).getiCardPosition()));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(CRC));
			
			this.setHS(HSP);
		}
		
		return bisFourOfAKind;
	}

	public boolean isFullHouse() {
		boolean bisFullHouse = false;
		
		if (CRC.get(0).getiCnt() == 3 && CRC.get(1).getiCnt() == 2) {
			bisFullHouse = true;

			HSP.seteHandStrength(eHandStrength.FullHouse);
			HSP.setHiCard(this.getCards().get(CRC.get(0).getiCardPosition()));
			HSP.setLoCard(this.getCards().get(CRC.get(1).getiCardPosition()));
			HSP.setKickers(null);
			
			this.setHS(HSP);
		}
		
		return bisFullHouse;
	}

	public boolean isFlush() {
		boolean bisFlush = false;
		int iCardCnt = super.getCards().size();
		int iSuitCnt = 0;

		for (eSuit eSuit : eSuit.values()) {
			for (Card c : super.getCards()) {
				if (eSuit == c.geteSuit()) {
					iSuitCnt++;
				}
			}
			if (iSuitCnt > 0)
				break;
		}

		if (iSuitCnt == iCardCnt) {
			bisFlush = true;
			
			HSP.seteHandStrength(eHandStrength.Flush);
			HSP.setHiCard(super.getCards().get(eCardNo.FIRST.getiCardNo()));
			HSP.setLoCard(null);
			HSP.setKickers(null);
			
			this.setHS(HSP);
		}	
		else
			bisFlush = false;
		
		return bisFlush;
	}

	public boolean isStraight() {
		boolean bisStraight = false;

		if (super.getCards().get(eCardNo.FIRST.getiCardNo()).geteRank() == eRank.ACE && 
				super.getCards().get(eCardNo.SECOND.getiCardNo()).geteRank() == eRank.FIVE) {

			//pattern from second card
			//first and second cards are checked
			for(int cardsIndex = 2; cardsIndex < this.getCards().size(); cardsIndex++) {
				if (this.getCards().get(cardsIndex).geteRank().getiRankNbr() + 1
						== this.getCards().get(cardsIndex-1).geteRank().getiRankNbr()) {
					bisStraight = true;

					HSP.seteHandStrength(eHandStrength.Straight);
					HSP.setHiCard(super.getCards().get(eCardNo.SECOND.getiCardNo()));
					HSP.setLoCard(null);
					HSP.setKickers(null);
					
					this.setHS(HSP);
				}
				else {
					// if ALL the cards in hand after the 1st card don't pass the condition
					// for the if statement, bisStraight becomes false again
					bisStraight = false;
					break;
				}
			}
		} else {
			for(int cardsIndex = 1; cardsIndex < this.getCards().size(); cardsIndex++) {
				if (this.getCards().get(cardsIndex).geteRank().getiRankNbr() + 1
						== this.getCards().get(cardsIndex-1).geteRank().getiRankNbr()) {
					bisStraight = true;

					HSP.seteHandStrength(eHandStrength.Straight);
					HSP.setHiCard(super.getCards().get(eCardNo.FIRST.getiCardNo()));
					HSP.setLoCard(null);
					HSP.setKickers(null);
					
					this.setHS(HSP);
				}
				else {
					//check if cards fulfill condiiton or are bisstraight
					bisStraight = false;
					break;
				}
			}
		}

		return bisStraight;
	}

	public boolean isThreeOfAKind() {
		boolean bisThreeOfAKind = false;

		if (CRC.get(1).getiCnt() == 1 && CRC.get(0).getiCnt() == 3) {
			bisThreeOfAKind = true;
			
			HSP.seteHandStrength(eHandStrength.ThreeOfAKind);
			HSP.setHiCard(super.getCards().get(CRC.get(0).getiCardPosition()));
			HSP.setLoCard(null);
			HSP.setKickers(FindTheKickers(this.getCRC()));

			this.setHS(HSP);
		}
		
		return bisThreeOfAKind;
	}

	public boolean isTwoPair() {
		boolean bisTwoPair = false;

		if (CRC.get(0).getiCnt() == 2 && CRC.get(1).getiCnt() == 2) {
			bisTwoPair = true;
			
			HSP.seteHandStrength(eHandStrength.TwoPair);
			HSP.setHiCard(this.getCards().get(CRC.get(0).getiCardPosition()));
			HSP.setLoCard(this.getCards().get(CRC.get(1).getiCardPosition()));
			HSP.setKickers(FindTheKickers(this.getCRC()));

			this.setHS(HSP);
		}

		return bisTwoPair;
	}

	public boolean isPair() {
		boolean bisPair = false;
		
			if (CRC.get(1).getiCnt() == 1 && this.getCRC().get(0).getiCnt() == 2) {
				bisPair = true;
				
				HSP.seteHandStrength(eHandStrength.Pair);
				HSP.setHiCard(this.getCards().get(getCRC().get(0).getiCardPosition()));
				HSP.setLoCard(null);
				HSP.setKickers(FindTheKickers(this.getCRC()));
				
				this.setHS(HSP);
			}
		
		return bisPair;
	}

	public boolean isHighCard() {
		boolean bisHighCard = false;
		if (CRC.get(0).getiCnt() == 1) {
			bisHighCard = true;
			
			HSP.seteHandStrength(eHandStrength.HighCard);
			HSP.setHiCard(this.getCards().get(getCRC().get(0).getiCardPosition()));
			HSP.setLoCard(getCards().get(CRC.get(1).getiCardPosition()));
			HSP.setKickers(FindTheKickers(this.getCRC()));
			
			this.setHS(HSP);
		}
		return bisHighCard;
	}
}