package game.controller;

import game.ai.AiBase;
import game.othello.Bord;

import java.util.ArrayList;

import common.Pos;
import common.Common.Stone;

/**
 * ゲーム進行を管理する
 *
 * @author 本体
 *
 */
public class GameController {

	private ArrayList<AiBase> PlayerList;

	public GameController() {
		PlayerList = new ArrayList<AiBase>();
	}

	public GameController(AiBase Player1, AiBase Player2) {
		PlayerList = new ArrayList<AiBase>();
		PlayerList.add(Player1);
		PlayerList.add(Player2);
	}

	public void GameStart() {
		boolean passFlg = false;
		int gameCnt = 0;
		Bord bord = new Bord();

		bord.BordInit();

		System.out.println("ゲームスタート");
		System.out.println(bord.toString());

		// おける場所がある間はループ
		while (bord.GetCount(Stone.NONE) > 0) {
			Pos setHand = new Pos();

			AiBase turnPlayer = PlayerList.get(gameCnt % 2);

			System.out.println(turnPlayer.getMyColor().getName() + "の番");

			setHand = turnPlayer.WhereSet(bord);
			if (setHand == null) {
				System.out.println(turnPlayer.getMyColor().getName() + "はパスします");

				if (passFlg) {
					System.out.println("どちらもパスしたため、ゲームを終了します。");
					break;
				}

				passFlg = true;
			} else {
				bord.DoSet(setHand, turnPlayer.getMyColor(), true);
				passFlg = false;
			}
			gameCnt++;
			System.out.println(bord.toString());
		}

		int player1Cnt = bord.GetCount(PlayerList.get(0).getMyColor());
		int player2Cnt = bord.GetCount(PlayerList.get(1).getMyColor());

		System.out.println(player1Cnt + ":" + player2Cnt);

		if (player1Cnt > player2Cnt) {
			System.out.println(PlayerList.get(0).getMyColor().getName() + "の勝利");
		} else if (player1Cnt == player2Cnt) {
			System.out.println("引き分け");
		} else {
			System.out.println(PlayerList.get(1).getMyColor().getName() + "の勝利");
		}

	}
}
