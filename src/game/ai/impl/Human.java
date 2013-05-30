package game.ai.impl;

import game.ai.AiBase;
import game.othello.Bord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import common.Common;
import common.Common.Stone;
import common.Pos;

public class Human extends AiBase {

	public Human(Stone MyColor) {
		super(MyColor);
	}

	@Override
	public Pos WhereSet(Bord bord) {

		int xPos =getSet("X");
		int yPos =getSet("Y");

		Pos retHand = new Pos(xPos,yPos);

		while(! bord.CanSet(retHand,getMyColor())){
			System.out.println("そこには置けません。選びなおしてください。");
			xPos =getSet("X");
			yPos =getSet("Y");
			retHand = new Pos(xPos,yPos);
		}

		return retHand;
	}

	/**
	 * どこにおくかキーボード入力を行う
	 * @param lineType
	 * @return
	 */
	private int getSet(String lineType) {
		int retVal = -1;
		while (retVal < 0) {

			// 標準出力
			System.out.println(lineType + "座標を入力してください(0～7)");

			// キーボードから文字を入力する時に、記述する
			BufferedReader obj_br = new BufferedReader(new InputStreamReader(System.in));

			//、キーボードから入力された文字を代入
			try {
				String str = obj_br.readLine();
				retVal = Integer.parseInt(str);

				//入力範囲を超えていないか判断
				if (retVal >= Common.X_MAX_LEN || retVal < Common.X_MIN_LEN) {
					System.out.print("座標は0～7で入力してください。");
					retVal = -1;
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (NumberFormatException ne) {
				ne.printStackTrace();
			}
		}
		return retVal;

	}

}
