package ru.untitled_devs.bot.shared.localisation;


public enum MessageKey {
	NO_USERNAME("no_username"),
	BANNED("banned"),
	NOT_REGISTERED("not_registered"),
	WHATS_YOUR_NAME("whats_your_name"),
	ASK_AGE("ask_age"),
	LOW_AGE("low_age"),
	AGE_ERROR("age_error"),
	ASK_LOCATION("ask_location"),
	ASK_DESCRIPTION("ask_description"),
	ASK_PHOTO("ask_photo"),
	PHOTO_REJECT("photo_reject"),
	THANKS_FOR_PHOTO("thanks_for_photo"),
	THANKS_FOR_VIDEO("thanks_for_video"),
	TOO_BIG_PHOTO("too_big_photo"),

	CANT_FIND_LOCATION("cant_find_location"),

	CREATE_PROFILE("create_profile"),
	SUCCESSFUL_REGISTRATION("successful_registration"),
	SUCCESSFUL_EDITED_PROFILE("successful_edited_profile"),
	CREATE_ACCOUNT_AGAIN("create_account_again"),
	PROFILE_PREVIEW("profile_preview"),

	MAIN_MENU("main_menu"),

	FORMS_NOT_FOUND("forms_not_found"),
	NO_FORMS("no_forms"),

	LIKED("liked"),
	YOU_HAVE_BEEN_LIKED("you_have_been_liked"),

	GO_TO_PROFILE("go_to_profile"),
	CANT_FIND_USER("cant_find_user"),

	SEND_REPORT_TEXT("send_report_text"),
	REPORT_SENT("report_sent"),

	INCORRECT_INPUT("incorrect_input"),

	CANT_SHOW_LIKE("cant_show_like"),
	LIKE_LIST_CLEAN("like_list_clean"),

	ENTER_NEW_NAME("enter_new_name"),
	NAME_CHANGED("name_changed"),

	ENTER_NEW_DESCRIPTION("enter_new_description"),
	DESCRIPTION_CHANGED("description_changed"),

	ENTER_NEW_AGE("enter_new_age"),
	AGE_CHANGED("age_changed"),

	ENTER_NEW_LOCATION("enter_new_location"),
	LOCATION_CHANGED("location_changed"),

	SEND_NEW_PHOTO("send_new_photo"),
	REMOVED_PHOTO("removed_photo"),

	ACCOUNT_REMOVED("account_removed"),
	PRESS_START_FOR_REGISTRATION("press_start_for_registration"),

	CHANGE_LANGUAGE("change_language"),

	ADMIN_MESSAGE("admin_message"),
	ADMIN_MODE_LEFT("admin_mode_left"),
	NO_REPORTS("no_reports"),
	CANT_SHOW_REPORT("cant_show_report"),
	REPORT_LIST_CLEAR("report_list_clear"),

	USER_REGISTERED_AS_ADMINISTRATOR("user_registered_as_administrator"),
	ADMIN_REMOVED("admin_removed"),

	USER_UNBANNED("user_unbanned"),
	USER_BANNED("user_banned"),

	USER_SHOWED("user_showed"),

	MESSAGE_SENT_TO_ALL_USERS("message_sent_to_all_users"),

	EDITING_CANCELED("editing_canceled"),

	NO_LIKES("no_likes"),
	LIKE_LIST("like_list"),

	DISLIKE("dislike"),

	CANT_UPDATE_PROFILE("cant_update_profile"),
	UPDATE_DONE("update_done"),

	YOUR_PROFILE("your_profile"),

	TOO_BIG_DESCRIPTION("too_big_description"),
	TOO_BIG_AGE("too_big_age"),

	REPORT_SEND_CANCELLED("report_send_cancelled"),
	INCORRECT_LOCATION_MESSAGE("incorrect_location_message"),

	NICE_TO_MEET_YOU("nice_to_meet_you"),
	SELECT_LANGUAGE("select_language"),

	PROFILE_MESSAGE("profile_message");


	private final String key;
	MessageKey(String key) { this.key = key; }
	public String key() { return key; }
}
