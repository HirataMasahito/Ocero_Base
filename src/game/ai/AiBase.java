package game.ai;


import game.othello.Bord;

import common.Common.Stone;
import common.Pos;

/**
 * AIの共通スーパークラス
 * @author 本体
 *
 */
public abstract class AiBase {

	/** Aiの使用色 */
	private Stone MyColor;

	/** AIの名前ー不要 */
	private String AiName;

	/**
	 * 引数憑きコンストラクタ
	 * @param MyColor AIの仕用色
	 */
	public AiBase(Stone MyColor){
		this.MyColor = MyColor;
	}

	/**
	 * 色の取得
	 * @return 使用色
	 */
	public Stone getMyColor(){
		return MyColor;
	}

	public String GetAiName(){
		return AiName;
	}


	/**
	 * どこにおくかを取得します。
	 *
	 * @return 設置場所
	 */
	abstract public Pos WhereSet(Bord bord);

}
