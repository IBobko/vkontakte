package ru.todo100.social.vk.strategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.todo100.social.vk.datas.DatabaseData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by igor on 22.03.15.
 */
public class VideoOperations extends Operations{
    public VideoOperations(String accessToken) {
        super(accessToken);
    }


//    video.getВозвращает информацию о видеозаписях.
//    video.editРедактирует данные видеозаписи на странице пользователя.
    public void add(Long target_id,Integer video_id,Integer owner_id) {
        try {
            StringBuilder urlString = getStringBuilder("video.add");
            urlString.append("&").append("owner_id").append("=").append(owner_id);
            urlString.append("&").append("video_id").append("=").append(video_id);
            urlString.append("&").append("target_id").append("=").append(target_id);
            String responseBody = getResponse(urlString.toString());
            JSONObject object = new JSONObject(responseBody);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    video.saveВозвращает адрес сервера (необходимый для загрузки) и данные видеозаписи.
//    video.deleteУдаляет видеозапись со страницы пользователя.
//    video.restoreВосстанавливает удаленную видеозапись.
//    video.searchВозвращает список видеозаписей в соответствии с заданным критерием поиска.
//    video.getUserVideosВозвращает список видеозаписей, на которых отмечен пользователь.
//    video.getAlbumsВозвращает список альбомов видеозаписей пользователя или сообщества.
//    video.getAlbumByIdПозволяет получить информацию об альбоме с видео.
//    video.addAlbumСоздает пустой альбом видеозаписей.
//    video.editAlbumРедактирует название альбома видеозаписей.
//    video.deleteAlbumУдаляет альбом видеозаписей.
//    video.reorderAlbumsПозволяет изменить порядок альбомов с видео.
//    video.addToAlbumПозволяет добавить видеозапись в альбом.
//    video.removeFromAlbumПозволяет убрать видеозапись из альбома.
//    video.getAlbumsByVideoВозвращает список альбомов, в которых находится видеозапись.
//    video.getCommentsВозвращает список комментариев к видеозаписи.
//    video.createCommentCоздает новый комментарий к видеозаписи
//    video.deleteCommentУдаляет комментарий к видеозаписи.
//    video.restoreCommentВосстанавливает удаленный комментарий к видеозаписи.
//    video.editCommentИзменяет текст комментария к видеозаписи.
//    video.getTagsВозвращает список отметок на видеозаписи.
//    video.putTagДобавляет отметку на видеозапись.
//    video.removeTagУдаляет отметку с видеозаписи.
//    video.getNewTagsВозвращает список видеозаписей, на которых есть непросмотренные отметки.
//    video.reportПозволяет пожаловаться на видеозапись.
//    video.reportCommentПозволяет пожаловаться на комментарий к видеозаписи.
}
