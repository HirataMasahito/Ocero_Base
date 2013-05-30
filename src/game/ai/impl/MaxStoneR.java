package game.ai.impl;

import java.util.ArrayList;

import common.Common.Stone;
import common.Common;
import common.Pos;

import game.ai.AiBase;
import game.othello.Bord;

public class MaxStoneR extends AiBase {

	public MaxStoneR(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord) {
		Pos retPos = null;

		ArrayList<ValuePos> handList = new ArrayList<ValuePos>();

		// おけるところすべてを取得してループをまわす
		ArrayList<Pos> canSetList = bord.getCanSetList(getMyColor());
		for (Pos pos : canSetList) {
			Bord vrBord = new Bord(bord);

			// 一度石を置く
			vrBord.DoSet(pos, getMyColor(), true);

			Pos workPos = getMaxPos(vrBord, Stone.reverseStone(getMyColor()));
			if (workPos != null) {
				// 相手がパスか判断
				vrBord.DoSet(workPos, Stone.reverseStone(getMyColor()), true);
			}

			workPos = getMaxPos(vrBord, getMyColor());
			if (workPos != null) {
				// 自分がパスか判断
				vrBord.DoSet(workPos, getMyColor(), true);
			}

			// 自分と相手の意思の差を取得する
			int getCnt = vrBord.GetCount(getMyColor()) - vrBord.GetCount(Stone.reverseStone(getMyColor()));

			handList.add(new ValuePos(getCnt, new Pos(pos.getX(), pos.getY())));

		}

		int maxCnt = Integer.MIN_VALUE;
		for (ValuePos valuePos : handList) {
			if (maxCnt < valuePos.getCnt()) {
				maxCnt = valuePos.getCnt();
				retPos = valuePos.getPos();
			}
		}

		return retPos;
	}



	private Pos getMaxPos(Bord bord, Stone color) {
		Pos retPos = null;
		Pos workPos = new Pos();
		ArrayList<ValuePos> handList = new ArrayList<ValuePos>();

		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			workPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				workPos.setX(x);
				int getCnt = bord.DoSet(workPos, getMyColor(), false);
				if (getCnt > 0) {
					handList.add(new ValuePos(getCnt, new Pos(x, y)));
				}
			}
		}

		int maxCnt = 0;
		for (ValuePos valuePos : handList) {
			if (maxCnt < valuePos.getCnt()) {
				maxCnt = valuePos.getCnt();
				retPos = valuePos.getPos();
			}
		}

		return retPos;
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

	@Override
	public String toString() {

		return "MaxStoneR";
	}
}
