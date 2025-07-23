package ru.untitled_devs.bot.shared.localisation;

public enum MessageKey {
	NO_USERNAME("no_username"),
	BANNED("banned"),
	NOT_REGISTERED("not_registered"),
	PRESS_START_FOR_REGISTRATION("press_start_for_registration"),
	CREATE_ACCOUNT_AGAIN("create_account_again"),

	WHATS_YOUR_NAME("whats_your_name"),
	ASK_AGE("ask_age"),
	LOW_AGE("low_age"),
	AGE_ERROR("age_error"),
	ASK_SEX("ask_sex"),
	ASK_LOCATION("ask_location"),
	CANT_FIND_LOCATION("cant_find_location"),
	ASK_DESCRIPTION("ask_description"),
	ASK_PHOTO("ask_photo"),
	PHOTO_REJECT("photo_reject"),
	THANKS_FOR_PHOTO("thanks_for_photo"),
	THANKS_FOR_VIDEO("thanks_for_video"),
	TOO_BIG_PHOTO("too_big_photo"),
	CREATE_PROFILE("create_profile"),
	SUCCESSFUL_REGISTRATION("successful_registration"),
	SUCCESSFUL_EDITED_PROFILE("successful_edited_profile"),

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
	CANT_UPDATE_PROFILE("cant_update_profile"),
	UPDATE_DONE("update_done"),
	ACCOUNT_REMOVED("account_removed"),

	TOO_BIG_NAME("too_big_name"),
	TOO_BIG_LOCATION("too_big_location"),
	TOO_BIG_DESCRIPTION("too_big_description"),
	TOO_BIG_AGE("too_big_age"),
	TOO_SMALL_AGE("too_small_age"),
	TOO_BIG_PHOTO_SIZE("too_big_photo_size"),

	MAIN_MENU("main_menu"),
	PROFILE_PREVIEW("profile_preview"),
	YOUR_PROFILE("your_profile"),
	PROFILE_MESSAGE("profile_message"),

	LIKED("liked"),
	YOU_HAVE_BEEN_LIKED("you_have_been_liked"),
	GO_TO_PROFILE("go_to_profile"),
	DISLIKE("dislike"),
	LIKE_LIST("like_list"),
	LIKE_LIST_CLEAN("like_list_clean"),
	CANT_SHOW_LIKE("cant_show_like"),
	NO_LIKES("no_likes"),

	SEND_REPORT_TEXT("send_report_text"),
	REPORT_SENT("report_sent"),
	REPORT_SEND_CANCELLED("report_send_cancelled"),
	NO_REPORTS("no_reports"),
	REPORT_LIST_CLEAR("report_list_clear"),
	CANT_SHOW_REPORT("cant_show_report"),

	INCORRECT_INPUT("incorrect_input"),
	FILE_DOWNLOADING_ERROR("file_downloading_error"),
	INCORRECT_LOCATION_MESSAGE("incorrect_location_message"),
	FORMS_NOT_FOUND("forms_not_found"),
	NO_FORMS("no_forms"),
	CANT_FIND_USER("cant_find_user"),
	EDITING_CANCELED("editing_canceled"),

	ADMIN_MESSAGE("admin_message"),
	ADMIN_MODE_LEFT("admin_mode_left"),
	USER_REGISTERED_AS_ADMINISTRATOR("user_registered_as_administrator"),
	ADMIN_REMOVED("admin_removed"),
	USER_BANNED("user_banned"),
	USER_UNBANNED("user_unbanned"),
	USER_SHOWED("user_showed"),
	MESSAGE_SENT_TO_ALL_USERS("message_sent_to_all_users"),

	CHANGE_LANGUAGE("change_language"),
	SELECT_LANGUAGE("select_language"),
	NICE_TO_MEET_YOU("nice_to_meet_you");

	private final String key;

	MessageKey(String key) {
		this.key = key;
	}

	public String key() {
		return key;
	}
}
