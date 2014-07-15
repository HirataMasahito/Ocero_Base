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

	/** プレイヤーリスト */
	private ArrayList<AiBase> PlayerList;

	/**
	 * コンストラクタ
	 */
	public GameController() {
		PlayerList = new ArrayList<AiBase>();
	}

	/**
	 * プレイヤー初期化コンストラクタ
	 * @param Player1 プレイヤー１のAIクラス
	 * @param Player2 プレイヤー２のAIクラス
	 */
	public GameController(AiBase Player1, AiBase Player2) {
		PlayerList = new ArrayList<AiBase>();
		PlayerList.add(Player1);
		PlayerList.add(Player2);
	}

	/**
	 * ゲームの開始
	 */
	public void GameStart() {
		boolean passFlg = false;
		int gameCnt = 0;
		Bord bord = new Bord();

		bord.BordInit();

		System.out.println("ゲームスタート");
		System.out.println(bord.toString());

		// おける場所がある間はループ
		while (bord.GetCount(Stone.NONE) > 0) {

			// 現在の手版を取得
			AiBase turnPlayer = PlayerList.get(gameCnt % 2);
			System.out.println(turnPlayer.getMyColor().getName() + "の番");

			//プレイヤーから石を置く位置を取得する。 AIが盤をいじらないようクローンを渡す
			Pos setHand = new Pos();
			setHand = turnPlayer.WhereSet(bord.clone(),null);

			//設置場所がNULLの場合パスとする
			if (setHand == null) {
				System.out.println(turnPlayer.getMyColor().getName() + "はパスします");

				//両プレイヤーがパスをした場合、ゲーム終了とする
				if (passFlg) {
					System.out.println("どちらもパスしたため、ゲームを終了します。");
					break;
				}

				passFlg = true;
			} else {
				// 石を設置する
				bord.DoSet(setHand, turnPlayer.getMyColor(), true);
				passFlg = false;
			}
			gameCnt++;
			//版情報を描画
			System.out.println(bord.toString());
		}
		//////
		//ゲーム終了-結果判断
		//////
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
