package game.othello;

import java.util.ArrayList;

import common.Common;
import common.Common.Stone;
import common.Pos;

/**
 * オセロの盤
 *
 * @author 本体
 *
 */
public class Bord {

	/** 盤 */
	private Stone[][] BORD_STATE;

	/** 最後に置いた位置 */
	private Pos LAST_SET_POS;

	/**
	 * 引数なしコンストラクタ
	 */
	public Bord() {
		BORD_STATE = new Stone[Common.X_MAX_LEN][Common.Y_MAX_LEN];
		BordInit();
	}

	/**
	 * 引数有りコンストラクタ
	 * 盤情報を複製します
	 *
	 * @param vrBord
	 *            ボード情報
	 */
	public Bord(Bord vrBord) {
		this.BORD_STATE = new Stone[Common.X_MAX_LEN][Common.Y_MAX_LEN];

		Pos workPos = new Pos();
		for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
			workPos.setX(x);
			for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
				workPos.setY(y);
				// 引数のボードから石を取得し、同じ位置にセットする

				setColor(workPos, vrBord.getColor(workPos));
			}
		}
	}

	/**
	 * 盤の情報を取得する
	 * @return
	 */
	public Stone[][] getBord() {
		return BORD_STATE;
	}

	/**
	 * 最後に置いた位置を取得する
	 * @return
	 */
	public Pos getLastSetPos(){
		return LAST_SET_POS;
	}

	/**
	 * 盤の全消去
	 */
	public void BordClean() {
		Pos workPos = new Pos();
		for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
			workPos.setX(x);
			for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
				workPos.setY(y);
				setColor(workPos, Stone.NONE);
			}
		}
	}

	/**
	 * 指定した座標に色をセットする
	 *
	 * @param setPos
	 *            座標
	 * @param Color
	 *            石
	 */
	private void setColor(Pos setPos, Stone Color) {
		BORD_STATE[setPos.getY()][setPos.getX()] = Color;
	}

	/**
	 * 指定した座標の石を取得
	 *
	 * @param setPos
	 *            座標
	 * @return 石
	 */
	public Stone getColor(Pos setPos) {
		return BORD_STATE[setPos.getY()][setPos.getX()];
	}

	/**
	 * 盤の初期化
	 * 初期の白黒も配置
	 */
	public void BordInit() {
		BordClean();
		setColor(new Pos(3, 3), Stone.WHITE);
		setColor(new Pos(4, 4), Stone.WHITE);
		setColor(new Pos(3, 4), Stone.BLACK);
		setColor(new Pos(4, 3), Stone.BLACK);
	}

	/**
	 * 石が置けるか否か判断し、おける場合取れる方向リストを取得します
	 *
	 * @param setPos
	 *            設置座標
	 * @param Color
	 *            指定した石の色
	 * @return 置けるならTrue
	 */
	public boolean CanSet(Pos setPos, Stone myColor) {
		// 第一条件 石がないこと
		if (getColor(setPos) == Stone.NONE) {

			ArrayList<Pos> directionList = Pos.getToAllDirection();

			for (Pos direction : directionList) {
				// 探索の開始位置をセット
				Pos nawPos = new Pos(setPos.getX(), setPos.getY());

				// 相手の石があったか
				boolean enemyFlg = false;

				// 移動できなくなるか、石が置ける場合は処理を終了
				while (nawPos.canMove(direction)) {
					nawPos.doMove(direction);

					// 探索先の石を取得
					Stone nawStone = getColor(nawPos);

					if (nawStone == Stone.NONE) {
						// 石がない場合は置けないため探索を終了
						break;
					} else if (nawStone == myColor) {
						if (enemyFlg) {
							// 自分の石かつ、敵の石が間にある場合
							// 設置可能な方向として戻り値にセットし、このループを終了
							return true;
						}
						break;

					} else if (nawStone != myColor) {
						// 敵の石の場合
						enemyFlg = true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 石が置けるか否か判断し、おける場合取れる方向リストを取得します
	 *
	 * @param setPos
	 *            設置座標
	 * @param Color
	 *            指定した石の色
	 * @return 設置可能な方向リスト
	 */
	public ArrayList<Pos> CanSetDirection(Pos setPos, Stone myColor) {
		ArrayList<Pos> retDirectionList = new ArrayList<Pos>();

		// 第一条件 石がないこと
		if (getColor(setPos) == Stone.NONE) {

			ArrayList<Pos> directionList = Pos.getToAllDirection();
			for (Pos direction : directionList) {
				// 探索の開始位置をセット
				Pos nawPos = new Pos(setPos.getX(), setPos.getY());

				// 相手の石があったか
				boolean enemyFlg = false;
				// 移動できなくなるか、石が置ける場合は処理を終了
				while (nawPos.canMove(direction)) {
					nawPos.doMove(direction);

					// 探索先の石を取得
					Stone nawStone = getColor(nawPos);

					if (nawStone == Stone.NONE) {
						// 石がない場合は置けないため探索を終了
						break;
					} else if (nawStone == myColor) {
						if (enemyFlg) {
							// 自分の石かつ、敵の石が間にある場合
							// 設置可能な方向として戻り値にセットし、このループを終了
							retDirectionList.add(direction);
						}
						break;
					} else if (nawStone != myColor) {
						// 敵の石の場合
						enemyFlg = true;
					}
				}
			}
		}
		return retDirectionList;
	}

	/**
	 * 石を設置する
	 *
	 * @param setPos
	 *            設置場所
	 * @param myColor
	 *            指定した石の色
	 * @param provisionalFlg
	 *            実際に石を反転するならTrue
	 * @return 反転する石の数
	 */
	public int DoSet(Pos setPos, Stone myColor, boolean provisionalFlg) {
		int reverseCnt = 0;

		// 設置可能な方向をすべて取得
		ArrayList<Pos> doSetDirection = CanSetDirection(setPos, myColor);

		// 設置可能な方向があるか判断
		if (doSetDirection.size() > 0) {
			// 設置開始位置に一石置くため、反転カウントに1加算
			reverseCnt++;
			if (provisionalFlg) {
				setColor(setPos, myColor);
			}
			// 最後に置いた位置を記録
			LAST_SET_POS = setPos;

			for (Pos direction : doSetDirection) {
				// 設置の開始位置をセット
				Pos nawPos = new Pos(setPos.getX(), setPos.getY());

				// 移動できなくなるか、石が置ける場合は処理を終了
				while (true) {
					nawPos.doMove(direction);

					// 探索先の石を取得
					Stone nawStone = getColor(nawPos);

					if (nawStone == Stone.NONE || nawStone == myColor) {
						// 自分の石の場合
						break;
					} else if (nawStone != myColor) {
						// 敵の石の場合
						if (provisionalFlg) {
							setColor(nawPos, myColor);
						}
						reverseCnt++;
					}
				}
			}
		}

		return reverseCnt;
	}

	/**
	 * 指定した色の石の個数を取得
	 *
	 * @param Color
	 *            色
	 * @return 指定した色の個数
	 */
	public Integer GetCount(Stone Color) {
		Integer retCnt = 0;
		Pos workPos = new Pos();
		for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
			workPos.setX(x);
			for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
				workPos.setY(y);
				if (getColor(workPos) == Color) {
					retCnt++;
				}
			}
		}

		return retCnt;
	}

	/**
	 * 石を置けるところすべてを取得します
	 *
	 * @param color
	 * @return
	 */
	public ArrayList<Pos> getCanSetList(Stone color) {
		ArrayList<Pos> retList = new ArrayList<Pos>();
		Pos workPos = new Pos();

		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			workPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				workPos.setX(x);

				if (CanSet(workPos, color)) {
					retList.add(new Pos(x, y));
				}
			}
		}
		return retList;
	}

	@Override
	public String toString() {
		// 現在の盤情報を文字列で取得する
		StringBuilder sb = new StringBuilder();

		Pos workPos = new Pos();
		for (int y = Common.Y_MIN_LEN; y < Common.Y_MAX_LEN; y++) {
			workPos.setY(y);
			for (int x = Common.X_MIN_LEN; x < Common.X_MAX_LEN; x++) {
				workPos.setX(x);
				switch (getColor(workPos)) {
				case NONE:
					sb.append("N");
					break;
				case BLACK:
					sb.append("B");
					break;
				case WHITE:
					sb.append("W");
					break;
				}
				sb.append("\t");
			}
			sb.append("\r\n");
		}

		return sb.toString();
	}

}
