package ru.todo100.social.vk.datas;

/**
 * Created by igor on 26.01.15.
 */
public class PostData {
    private Long id;
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

//    id: 4593,
//    from_id: 99991,
//    owner_id: 99991,
//    date: 1422261192,
//    post_type: 'post',
//    text: 'тыц тыц тыц',
//    can_edit: 1,
//    can_delete: 1,
//    can_pin: 1,
//    post_source: {
//        type: 'api'
//    },
//    comments: {
//        count: 0,
//                can_post: 1
//    },
//    likes: {
//        count: 0,
//                user_likes: 0,
//                can_like: 1,
//                can_publish: 0
//    },
//    reposts: {
//        count: 0,
//                user_reposted: 0
//    }
}
