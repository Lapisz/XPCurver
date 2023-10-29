package net.lapisz.xpcurver;

import net.fabricmc.api.ModInitializer;

import net.lapisz.xpcurver.config.XPCConfig;
import net.lapisz.xpcurver.util.ExpressionTree;
import net.lapisz.xpcurver.util.Interpreter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

// Don't question why I am naming in camelcase most of the time

public class XPCurver implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("xpcurver");
	public static final XPCConfig CONFIG = XPCConfig.createAndLoad();

	public static ExpressionTree ex_tree_1_15 = null;
	public static ExpressionTree ex_tree_16_30 = null;
	public static ExpressionTree ex_tree_31_plus = null;

	private void log_error(ArrayList<String> lex, String levels) {
		if (lex == null) {
			LOGGER.error("Error lexing formula for levels " + levels);
			LOGGER.error("(This error normally should never happen)");
			return;
		}

		String expression = "";
		for (String s : lex) {
			expression += " " + s;
		}
		if (expression.length() > 0 && expression.charAt(0) == ' ') {
			expression = expression.substring(1);
		}

		String indicator = "";
		for (int j = 0; j < Interpreter.err_pos; j++) {
			for (int k = 0; k < lex.get(j).length() + 1; k++) {
				indicator += " ";
			}
		}
		indicator = indicator + "^";

		int indicator_length = indicator.length();
		if (indicator_length < 44) {
			indicator += " (error here)";
		} else {
			indicator = indicator.substring(0, indicator_length - 14) + "(error here) ^";
		}

		String err_msg;
		int err_type = Interpreter.err_type;
		switch (err_type) {
			case 1:
				err_msg = "Error: Extra unprocessed characters at end of string";
				break;
			case 2:
				err_msg = "Error: Expected a numerical value or expression here";
				break;
			case 3:
				err_msg = "Error: Expected a \")\" here (unbalanced parentheses)";
				break;
			case 4:
				err_msg = "Error: Unexpected character (check your formatting)";
				break;
			default:
				err_msg = "Error: Other error (report to mod developer)";
		}

		LOGGER.error("An error has occurred while parsing the formula for");
		LOGGER.error("levels " + levels + "! Currently configured expression:");
		LOGGER.error(expression);
		LOGGER.error(indicator);
		LOGGER.error(err_msg);
		LOGGER.error("Falling back to the default formula for levels " + levels + ".");
	}

	@Override
	public void onInitialize() {
		String raw_str_1_15 = CONFIG.level_up_xp_1_15();
		String raw_str_16_30 = CONFIG.level_up_xp_16_30();
		String raw_str_31_plus = CONFIG.level_up_xp_31_plus();

		ArrayList<String> lex_1_15 = null;
		ArrayList<String> lex_16_30 = null;
		ArrayList<String> lex_31_plus = null;

		try {
			lex_1_15 = Interpreter.lex(raw_str_1_15);
			ex_tree_1_15 = Interpreter.parse(new ArrayList<>(lex_1_15));
		} catch (Exception e) {
			ex_tree_1_15 = null;
			log_error(lex_1_15, "1-15");
		}

		try {
			lex_16_30 = Interpreter.lex(raw_str_16_30);
			ex_tree_16_30 = Interpreter.parse(new ArrayList<>(lex_16_30));
		} catch (Exception e) {
			ex_tree_16_30 = null;
			log_error(lex_16_30, "16-30");
		}

		try {
			lex_31_plus = Interpreter.lex(raw_str_31_plus);
			ex_tree_31_plus = Interpreter.parse(new ArrayList<>(lex_31_plus));
		} catch (Exception e) {
			ex_tree_31_plus = null;
			log_error(lex_31_plus, "31+");
		}

		LOGGER.info("Finished attempting to parse all XP level formulas");
	}
}