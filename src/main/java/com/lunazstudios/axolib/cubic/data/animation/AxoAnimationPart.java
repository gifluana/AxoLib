package com.lunazstudios.axolib.cubic.data.animation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lunazstudios.axolib.cubic.keyframes.MolangKeyframe;
import com.lunazstudios.axolib.cubic.keyframes.MolangKeyframeChannel;
import com.lunazstudios.axolib.math.Constant;
import com.lunazstudios.axolib.math.molang.MolangParser;
import com.lunazstudios.axolib.math.molang.expressions.MolangExpression;
import com.lunazstudios.axolib.math.molang.expressions.MolangValue;

public class AxoAnimationPart {
    public final MolangKeyframeChannel tx = new MolangKeyframeChannel();
    public final MolangKeyframeChannel ty = new MolangKeyframeChannel();
    public final MolangKeyframeChannel tz = new MolangKeyframeChannel();
    public final MolangKeyframeChannel rx = new MolangKeyframeChannel();
    public final MolangKeyframeChannel ry = new MolangKeyframeChannel();
    public final MolangKeyframeChannel rz = new MolangKeyframeChannel();
    public final MolangKeyframeChannel sx = new MolangKeyframeChannel();
    public final MolangKeyframeChannel sy = new MolangKeyframeChannel();
    public final MolangKeyframeChannel sz = new MolangKeyframeChannel();

    public void fromJson(com.google.gson.JsonObject obj, MolangParser parser) {
        if (obj.has("translate")) parseTriple(tx, ty, tz, obj.get("translate"), parser);
        if (obj.has("position"))  parseTriple(tx, ty, tz, obj.get("position"),  parser);
        if (obj.has("rotate"))    parseTriple(rx, ry, rz, obj.get("rotate"),    parser);
        if (obj.has("rotation"))  parseTriple(rx, ry, rz, obj.get("rotation"),  parser);
        if (obj.has("scale"))     parseTriple(sx, sy, sz, obj.get("scale"),     parser);
    }

    private void parseTriple(MolangKeyframeChannel cx, MolangKeyframeChannel cy, MolangKeyframeChannel cz,
                              JsonElement el, MolangParser parser) {
        if (el.isJsonArray()) {
            JsonArray list = el.getAsJsonArray();

            if (isVector(list)) {
                addTriple(cx, cy, cz, 0f, "linear", list.get(0), list.get(1), list.get(2), parser);
                return;
            }

            for (JsonElement entry : list) {
                if (!entry.isJsonArray()) continue;
                JsonArray kf = entry.getAsJsonArray();
                if (kf.size() < 5) continue;

                float tick    = kf.get(0).getAsFloat() * 20f;
                String interp = kf.get(1).getAsString();
                addTriple(cx, cy, cz, tick, interp, kf.get(2), kf.get(3), kf.get(4), parser);
            }

            return;
        }

        if (!el.isJsonObject()) return;

        JsonObject obj = el.getAsJsonObject();
        ParsedVector direct = parseAnimationVector(obj, parser);
        if (direct != null) {
            addTriple(cx, cy, cz, 0f, direct.interpolation, direct.x, direct.y, direct.z);
            return;
        }

        for (var entry : obj.entrySet()) {
            float tick;
            try {
                tick = Float.parseFloat(entry.getKey()) * 20f;
            } catch (NumberFormatException ignored) {
                continue;
            }

            ParsedVector vector = parseAnimationVector(entry.getValue(), parser);
            if (vector != null) {
                addTriple(cx, cy, cz, tick, vector.interpolation, vector.x, vector.y, vector.z);
            }
        }
    }

    private void addTriple(MolangKeyframeChannel cx, MolangKeyframeChannel cy, MolangKeyframeChannel cz,
                           float tick, String interp, JsonElement x, JsonElement y, JsonElement z, MolangParser parser) {
        addTriple(cx, cy, cz, tick, interp, parseExpr(x, parser), parseExpr(y, parser), parseExpr(z, parser));
    }

    private void addTriple(MolangKeyframeChannel cx, MolangKeyframeChannel cy, MolangKeyframeChannel cz,
                           float tick, String interp, MolangExpression xVal, MolangExpression yVal, MolangExpression zVal) {
        MolangKeyframe kfx = new MolangKeyframe(tick, xVal);
        MolangKeyframe kfy = new MolangKeyframe(tick, yVal);
        MolangKeyframe kfz = new MolangKeyframe(tick, zVal);
        kfx.interpolation = interp;
        kfy.interpolation = interp;
        kfz.interpolation = interp;

        cx.add(kfx);
        cy.add(kfy);
        cz.add(kfz);
    }

    private ParsedVector parseAnimationVector(JsonElement element, MolangParser parser) {
        JsonArray array = element.isJsonArray() ? element.getAsJsonArray() : null;
        JsonObject object = element.isJsonObject() ? element.getAsJsonObject() : null;

        if (array == null && object != null) {
            if (object.has("vector") && object.get("vector").isJsonArray()) {
                array = object.getAsJsonArray("vector");
            } else if (object.has("post")) {
                JsonElement post = object.get("post");
                if (post.isJsonArray()) {
                    array = post.getAsJsonArray();
                } else if (post.isJsonObject() && post.getAsJsonObject().has("vector")) {
                    JsonElement vector = post.getAsJsonObject().get("vector");
                    if (vector.isJsonArray()) {
                        array = vector.getAsJsonArray();
                    }
                }
            }
        }

        if (array == null || !isVector(array)) return null;

        String interpolation = object == null ? "linear" : parseInterpolation(object);

        return new ParsedVector(
            parseExpr(array.get(0), parser),
            parseExpr(array.get(1), parser),
            parseExpr(array.get(2), parser),
            interpolation
        );
    }

    private boolean isVector(JsonArray array) {
        return array.size() >= 3 && !array.get(0).isJsonArray() && !array.get(0).isJsonObject();
    }

    private String parseInterpolation(JsonObject object) {
        if (object.has("lerp_mode") && object.get("lerp_mode").isJsonPrimitive()) {
            String mode = object.get("lerp_mode").getAsString();
            if ("catmullrom".equals(mode) || "catmull_rom".equals(mode)) {
                return "catmullrom";
            }
        }

        if (object.has("easing") && object.get("easing").isJsonPrimitive()) {
            return object.get("easing").getAsString();
        }

        return "linear";
    }

    private MolangExpression parseExpr(JsonElement el, MolangParser parser) {
        if (el.isJsonPrimitive()) {
            var prim = el.getAsJsonPrimitive();
            if (prim.isNumber()) {
                return new MolangValue(parser, new Constant(prim.getAsDouble()));
            }
            String expr = prim.getAsString();
            try {
                return parser.parseExpression(expr);
            } catch (Exception e) {
                return new MolangValue(parser, new Constant(0));
            }
        }
        return new MolangValue(parser, new Constant(0));
    }

    private record ParsedVector(MolangExpression x, MolangExpression y, MolangExpression z, String interpolation) {}
}
