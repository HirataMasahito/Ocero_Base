package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Common;
import common.Common.Stone;
import common.Pos;

public class MaxStone extends AiBase {

	public MaxStone(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord) {
		Pos retPos = null;
		Pos workPos = new Pos();
		ArrayList<ValuePos> handList = new ArrayList<ValuePos>();

		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			workPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				workPos.setX(x);
				int getCnt = bord.DoSet(workPos, getMyColor(), false);
				if (getCnt > 0) {
					handList.add(new ValuePos(getCnt, new Pos(x,y)));
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

		return "MaxStone";
	}

}
