package tk.shanebee.bee.elements.board.expression;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.doc.Since;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tk.shanebee.bee.elements.board.objects.BeeTeam;

@Name("Team - State")
@Description("Represents the friendly fire and can see friendly invisibles states of a team.")
@Examples("set allow friendly fire team state of team \"a-team\" to true")
@Since("INSERT VERSION")
public class ExprTeamState extends SimplePropertyExpression<BeeTeam, Boolean> {

    static {
        register(ExprTeamState.class, Boolean.class,
                "(0¦allow friendly fire|1¦can see friendly invisibles) team state",
                "beeteams");
    }

    private int pattern;

    @Override
    public boolean init(Expression<?> @NotNull [] exprs, int matchedPattern, @NotNull Kleenean isDelayed, ParseResult parseResult) {
        this.pattern = parseResult.mark;
        return super.init(exprs, matchedPattern, isDelayed, parseResult);
    }


    @Nullable
    @Override
    public Boolean convert(@NotNull BeeTeam beeTeam) {
        return pattern == 0 ? beeTeam.isFriendlyFire() : beeTeam.isFriendlyInvisibles();
    }

    @Nullable
    @Override
    public Class<?>[] acceptChange(@NotNull ChangeMode mode) {
        if (mode == ChangeMode.SET) {
            return CollectionUtils.array(Boolean.class);
        }
        return null;
    }

    @Override
    public void change(@NotNull Event event, @Nullable Object[] delta, @NotNull ChangeMode mode) {
        if (delta == null) return;

        boolean state = ((boolean) delta[0]);
        for (BeeTeam team : getExpr().getArray(event)) {
            if (pattern == 0) {
                team.setFriendlyFire(state);
            } else {
                team.setFriendlyInvisibles(state);
            }
        }
    }

    @Override
    public @NotNull Class<? extends Boolean> getReturnType() {
        return Boolean.class;
    }

    @Override
    protected @NotNull String getPropertyName() {
        String state = pattern == 0 ? "allow friendly fire" : "can see friendly invisibles";
        return state + " team state";
    }

}
