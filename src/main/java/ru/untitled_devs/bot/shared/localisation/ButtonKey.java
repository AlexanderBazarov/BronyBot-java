package ru.untitled_devs.bot.shared.localisation;

public enum ButtonKey {
	CREATE_PROFILE("create_profile"),
	REWRITE_PROFILE("rewrite_profile"),
	REMOVE_PROFILE("remove_profile"),
	CHANGE_NAME("change_name"),
	CHANGE_AGE("change_age"),
	CHANGE_DESCRIPTION("change_description"),
	CHANGE_CITY("change_city"),
	CHANGE_IMAGE("change_image"),
	REMOVE_IMAGE("remove_image"),
	UPDATE_FORM("update_form"),
	CHANGE_LANGUAGE("change_language"),

	REFUSE("refuse"),
	CANCEL("cancel"),
	SKIP_WORD("skip_word"),

	DONE("done"),

	MY_FORM("my_form"),
	VIEW_FORMS("view_forms"),
	VIEW_LIKES("view_likes"),

	SEND_LOCATION("send_location"),

	LIKE("like"),
	DISLIKE("dislike"),
	REPORT("report"),
	SKIP("skip"),
	BAN("ban"),

	BACK("back");

	private final String key;

	ButtonKey(String key) {
		this.key = key;
	}

	public String key() {
		return key;
	}
}
