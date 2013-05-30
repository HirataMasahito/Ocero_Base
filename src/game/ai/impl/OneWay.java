package game.ai.impl;

import common.Common.Stone;
import common.Common;
import common.Pos;

import game.ai.AiBase;
import game.othello.Bord;

public class OneWay extends AiBase {

	public OneWay(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord) {
		Pos retPos = new Pos();

		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			retPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				retPos.setX(x);

				if (bord.CanSet(retPos, getMyColor())) {
					return retPos;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "OneWay";
	}
}
