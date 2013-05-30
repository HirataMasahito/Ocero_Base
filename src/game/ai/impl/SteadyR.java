package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import common.Common.Stone;
import common.Pos;

public class SteadyR extends AiBase {
	/** 優先度 高 */
	private ArrayList<Pos> HighWeightList;

	/** 自分の状態 超優勢 */
	private static int MY_STATUS_VERY_GOOD = 4;
	/** 自分の状態 優勢 */
	private static int MY_STATUS_GOOD = 2;
	/** 自分の状態 対等 */
	private static int MY_STATUS_EQUAL = 0;
	/** 自分の状態 劣勢 */
	private static int My_STATUS_BAD = -3;

	private static int REFLEX_CNT = 3;

	private static int LCNT;

	public SteadyR(Stone MyColor) {
		super(MyColor);

		HighWeightList = new ArrayList<Pos>();
		HighWeightList.add(new Pos(0, 0));
		HighWeightList.add(new Pos(0, 7));
		HighWeightList.add(new Pos(7, 0));
		HighWeightList.add(new Pos(7, 7));
	}

	@Override
	public Pos WhereSet(Bord bord) {
		Pos retPos = null;

		ArrayList<Pos> myHands = bord.getCanSetList(getMyColor());
		if (myHands.size() > 0) {

			// 四隅だけは優先して置くように
			if (HighWeightList.containsAll(myHands)) {
				for (Pos pos : HighWeightList) {
					if (bord.CanSet(pos, getMyColor())) {
						retPos = pos;
						break;
					}
				}
			} else {
				LCNT = 0;
				// 四隅に置けないなら、堅実にして置くように
				ArrayList<ValuePos> valueList = new ArrayList<ValuePos>();
				for (Pos pos : myHands) {
					int value = getMyStatus(bord, pos, getMyColor(), REFLEX_CNT);
					valueList.add(new ValuePos(value, pos));
					//System.out.println(pos.getX() + ":" + pos.getY() + "=" + value);
				}

				// リストを降順でソートする
				Collections.sort(valueList, new ValuePosComparator());
				// ソート順の最高の値を取得
				retPos = valueList.get(0).getPos();

				//System.out.println("探索経路数:"+LCNT);
			}

		}

		return retPos;
	}

	public int getMyStatus(Bord bord, Pos setPos, Stone color, int cnt) {
		int retValue = 0;

		// 仮想ボードを作成する
		Bord vrBord = new Bord(bord);
		vrBord.DoSet(setPos, color, true);
		if (cnt < 0) {
			LCNT ++;
			// 自分と相手の数から状態を取得する
			int myHand = vrBord.GetCount(getMyColor());
			int enHand = vrBord.GetCount(Stone.reverseStone(getMyColor()));

			int nawStatus = myHand - enHand;
			// NULLをセットして少しでも軽く
			vrBord = null;
			if (nawStatus > 0) {
				if (myHand > enHand * 1.5) {
					return MY_STATUS_VERY_GOOD;
				} else {
					return MY_STATUS_GOOD;
				}
			} else if (nawStatus < 0) {
				return My_STATUS_BAD;
			} else {
				return MY_STATUS_EQUAL;
			}

		} else {
			// 色を逆転して、次に相手が置ける位置を選択する
			ArrayList<Pos> nextHandList = vrBord.getCanSetList(Stone.reverseStone(color));
			for (Pos pos : nextHandList) {
				int nextCnt = cnt - 1;
				retValue += getMyStatus(vrBord, pos, Stone.reverseStone(color), nextCnt);
			}

		}

		return retValue;
	}

	private class ValuePos {
		int cnt;
		Pos pos;

		ValuePos(int cnt, Pos pos) {
			this.cnt = cnt;
			this.pos = pos;
		}

		int getCnt() {
			return cnt;
		}

		Pos getPos() {
			return pos;
		}
	}

	class ValuePosComparator implements Comparator<ValuePos> {

		// 比較メソッド（データクラスを比較して-1, 0, 1を返すように記述する）
		public int compare(ValuePos a, ValuePos b) {
			int no1 = a.getCnt();
			int no2 = b.getCnt();

			// 降順でソートされる
			if (no1 > no2) {
				return -1;

			} else if (no1 == no2) {
				return 0;

			} else {
				return 1;

			}
		}

	}

	@Override
	public String toString() {
		return "SteadyR(遅いので注意)";
	}
}
