/**
 * Created by Jason on 2015/6/25.
 * 脚本中语言替换方法
 */
function L(tag) {
    if (typeof $_lang[tag] == "undefined") {
        return tag;
    }
    return $_lang[tag];
}