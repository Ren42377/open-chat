package io.openchat.ui.theme

import androidx.annotation.DrawableRes
import io.openchat.R

data class DesignSystemIconEntry(
    val id: String,
    val label: String,
    @param:DrawableRes val drawableRes: Int,
)

/**
 * Complete registry of the 48 design system vector icons in `res/drawable/ds_*.xml`.
 */
object OpenChatIcons {
    @DrawableRes val Emblem = R.drawable.ds_openchat_emblem_24
    @DrawableRes val ArrowCurvedLeft = R.drawable.ds_arrow_curvedpath_left_lg_regular_24
    @DrawableRes val ArrowCurvedRight = R.drawable.ds_arrow_curvedpath_right_lg_regular_24
    @DrawableRes val BalanceScale = R.drawable.ds_balance_scale_regular_24
    @DrawableRes val BarChart = R.drawable.ds_bar_chart_regular_24
    @DrawableRes val Book = R.drawable.ds_book_regular_24
    @DrawableRes val Braces = R.drawable.ds_braces_regular_24
    @DrawableRes val Brain = R.drawable.ds_brain_regular_24
    @DrawableRes val Camera = R.drawable.ds_camera_regular_24
    @DrawableRes val ChatBubble = R.drawable.ds_chat_bubble_regular_24
    @DrawableRes val CircleDollar = R.drawable.ds_circle_dollarsign_regular_24
    @DrawableRes val CircleStopFill = R.drawable.ds_circle_stop_fill_regular_24
    @DrawableRes val Codex = R.drawable.ds_codex_regular_24
    @DrawableRes val DeskGlobe = R.drawable.ds_desk_globe_regular_24
    @DrawableRes val Dumbbell = R.drawable.ds_dumbbell_regular_24
    @DrawableRes val Flask = R.drawable.ds_flask_regular_24
    @DrawableRes val Folder = R.drawable.ds_folder_regular_24
    @DrawableRes val Globe = R.drawable.ds_globe_regular_24
    @DrawableRes val GraduationCap = R.drawable.ds_graduation_cap_regular_24
    @DrawableRes val Heart = R.drawable.ds_heart_regular_24
    @DrawableRes val Kettlebell = R.drawable.ds_kettlebell_regular_24
    @DrawableRes val LaptopConnector = R.drawable.ds_laptop_connector_regular_24
    @DrawableRes val Lotus = R.drawable.ds_lotus_regular_24
    @DrawableRes val MicrophoneFill = R.drawable.ds_microphone_fill_regular_24
    @DrawableRes val Microphone = R.drawable.ds_microphone_regular_24
    @DrawableRes val MicrophoneSlashFill = R.drawable.ds_microphone_slash_fill_regular_24
    @DrawableRes val MicrophoneSlash = R.drawable.ds_microphone_slash_regular_24
    @DrawableRes val MusicNote = R.drawable.ds_music_note_regular_24
    @DrawableRes val Notebook = R.drawable.ds_notebook_regular_24
    @DrawableRes val PaintbrushOnPencil = R.drawable.ds_paintbrush_on_pencil_regular_24
    @DrawableRes val PaintPalette = R.drawable.ds_paint_palette_regular_24
    @DrawableRes val Paw = R.drawable.ds_paw_regular_24
    @DrawableRes val Pencil = R.drawable.ds_pencil_regular_24
    @DrawableRes val PenTip = R.drawable.ds_pen_tip_regular_24
    @DrawableRes val Personalization = R.drawable.ds_personalization_regular_24
    @DrawableRes val PhotoTiltedSquare = R.drawable.ds_photo_on_tilted_square_regular_24
    @DrawableRes val Photo = R.drawable.ds_photo_regular_24
    @DrawableRes val PlaneUpRight = R.drawable.ds_plane_up_right_regular_24
    @DrawableRes val Plant = R.drawable.ds_plant_regular_24
    @DrawableRes val Popcorn = R.drawable.ds_popcorn_regular_24
    @DrawableRes val ShareAndroid = R.drawable.ds_share_android_regular_24
    @DrawableRes val StarOfLife = R.drawable.ds_star_of_life_regular_24
    @DrawableRes val Stethoscope = R.drawable.ds_stethoscope_regular_24
    @DrawableRes val StopFill = R.drawable.ds_stop_fill_regular_24
    @DrawableRes val Suitcase = R.drawable.ds_suitcase_regular_24
    @DrawableRes val Terminal = R.drawable.ds_terminal_regular_24
    @DrawableRes val VoiceButton = R.drawable.ds_voice_button_regular_24
    @DrawableRes val Wrench = R.drawable.ds_wrench_regular_24

    val AllCatalog: List<DesignSystemIconEntry> = listOf(
        DesignSystemIconEntry("ds_openchat_emblem_24", "Open Chat Emblem", Emblem),
        DesignSystemIconEntry("ds_arrow_curvedpath_left_lg_regular_24", "Curved Left Arrow", ArrowCurvedLeft),
        DesignSystemIconEntry("ds_arrow_curvedpath_right_lg_regular_24", "Curved Right Arrow", ArrowCurvedRight),
        DesignSystemIconEntry("ds_balance_scale_regular_24", "Balance Scale", BalanceScale),
        DesignSystemIconEntry("ds_bar_chart_regular_24", "Bar Chart", BarChart),
        DesignSystemIconEntry("ds_book_regular_24", "Book", Book),
        DesignSystemIconEntry("ds_braces_regular_24", "Code Braces", Braces),
        DesignSystemIconEntry("ds_brain_regular_24", "Memory Brain", Brain),
        DesignSystemIconEntry("ds_camera_regular_24", "Camera", Camera),
        DesignSystemIconEntry("ds_chat_bubble_regular_24", "Chat Bubble", ChatBubble),
        DesignSystemIconEntry("ds_circle_dollarsign_regular_24", "Finance Dollar", CircleDollar),
        DesignSystemIconEntry("ds_circle_stop_fill_regular_24", "Circle Stop", CircleStopFill),
        DesignSystemIconEntry("ds_codex_regular_24", "Codex Agent", Codex),
        DesignSystemIconEntry("ds_desk_globe_regular_24", "Desk Globe", DeskGlobe),
        DesignSystemIconEntry("ds_dumbbell_regular_24", "Dumbbell", Dumbbell),
        DesignSystemIconEntry("ds_flask_regular_24", "Experiments Flask", Flask),
        DesignSystemIconEntry("ds_folder_regular_24", "Project Folder", Folder),
        DesignSystemIconEntry("ds_globe_regular_24", "Web Search Globe", Globe),
        DesignSystemIconEntry("ds_graduation_cap_regular_24", "Study & Learn", GraduationCap),
        DesignSystemIconEntry("ds_heart_regular_24", "Favorites Heart", Heart),
        DesignSystemIconEntry("ds_kettlebell_regular_24", "Kettlebell", Kettlebell),
        DesignSystemIconEntry("ds_laptop_connector_regular_24", "Remote Desktop Connector", LaptopConnector),
        DesignSystemIconEntry("ds_lotus_regular_24", "Wellbeing Lotus", Lotus),
        DesignSystemIconEntry("ds_microphone_fill_regular_24", "Microphone Fill", MicrophoneFill),
        DesignSystemIconEntry("ds_microphone_regular_24", "Microphone", Microphone),
        DesignSystemIconEntry("ds_microphone_slash_fill_regular_24", "Muted Fill", MicrophoneSlashFill),
        DesignSystemIconEntry("ds_microphone_slash_regular_24", "Muted", MicrophoneSlash),
        DesignSystemIconEntry("ds_music_note_regular_24", "Audio Note", MusicNote),
        DesignSystemIconEntry("ds_notebook_regular_24", "Pages Notebook", Notebook),
        DesignSystemIconEntry("ds_paintbrush_on_pencil_regular_24", "Sketch & Annotate", PaintbrushOnPencil),
        DesignSystemIconEntry("ds_paint_palette_regular_24", "Theme Palette", PaintPalette),
        DesignSystemIconEntry("ds_paw_regular_24", "Codex Companion Pet", Paw),
        DesignSystemIconEntry("ds_pencil_regular_24", "Compose / Edit", Pencil),
        DesignSystemIconEntry("ds_pen_tip_regular_24", "Canvas Pen Tip", PenTip),
        DesignSystemIconEntry("ds_personalization_regular_24", "Personalization", Personalization),
        DesignSystemIconEntry("ds_photo_on_tilted_square_regular_24", "Image Library", PhotoTiltedSquare),
        DesignSystemIconEntry("ds_photo_regular_24", "Photo", Photo),
        DesignSystemIconEntry("ds_plane_up_right_regular_24", "Travel / Send", PlaneUpRight),
        DesignSystemIconEntry("ds_plant_regular_24", "Growth / Seedy", Plant),
        DesignSystemIconEntry("ds_popcorn_regular_24", "Entertainment", Popcorn),
        DesignSystemIconEntry("ds_share_android_regular_24", "Share", ShareAndroid),
        DesignSystemIconEntry("ds_star_of_life_regular_24", "Safety / Emergency", StarOfLife),
        DesignSystemIconEntry("ds_stethoscope_regular_24", "Clinician / Health", Stethoscope),
        DesignSystemIconEntry("ds_stop_fill_regular_24", "Stop Generating", StopFill),
        DesignSystemIconEntry("ds_suitcase_regular_24", "Work Mode", Suitcase),
        DesignSystemIconEntry("ds_terminal_regular_24", "Terminal / CLI", Terminal),
        DesignSystemIconEntry("ds_voice_button_regular_24", "Realtime Voice", VoiceButton),
        DesignSystemIconEntry("ds_wrench_regular_24", "Tools & Plugins", Wrench),
    )
}
