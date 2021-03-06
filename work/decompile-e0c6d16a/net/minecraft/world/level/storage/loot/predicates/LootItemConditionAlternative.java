package net.minecraft.world.level.storage.loot.predicates;

import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.util.ChatDeserializer;
import net.minecraft.world.level.storage.loot.LootCollector;
import net.minecraft.world.level.storage.loot.LootSerializer;
import net.minecraft.world.level.storage.loot.LootTableInfo;

public class LootItemConditionAlternative implements LootItemCondition {

    final LootItemCondition[] terms;
    private final Predicate<LootTableInfo> composedPredicate;

    LootItemConditionAlternative(LootItemCondition[] alootitemcondition) {
        this.terms = alootitemcondition;
        this.composedPredicate = LootItemConditions.orConditions(alootitemcondition);
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.ALTERNATIVE;
    }

    public final boolean test(LootTableInfo loottableinfo) {
        return this.composedPredicate.test(loottableinfo);
    }

    @Override
    public void validate(LootCollector lootcollector) {
        LootItemCondition.super.validate(lootcollector);

        for (int i = 0; i < this.terms.length; ++i) {
            this.terms[i].validate(lootcollector.forChild(".term[" + i + "]"));
        }

    }

    public static LootItemConditionAlternative.a alternative(LootItemCondition.a... alootitemcondition_a) {
        return new LootItemConditionAlternative.a(alootitemcondition_a);
    }

    public static class a implements LootItemCondition.a {

        private final List<LootItemCondition> terms = Lists.newArrayList();

        public a(LootItemCondition.a... alootitemcondition_a) {
            LootItemCondition.a[] alootitemcondition_a1 = alootitemcondition_a;
            int i = alootitemcondition_a.length;

            for (int j = 0; j < i; ++j) {
                LootItemCondition.a lootitemcondition_a = alootitemcondition_a1[j];

                this.terms.add(lootitemcondition_a.build());
            }

        }

        @Override
        public LootItemConditionAlternative.a or(LootItemCondition.a lootitemcondition_a) {
            this.terms.add(lootitemcondition_a.build());
            return this;
        }

        @Override
        public LootItemCondition build() {
            return new LootItemConditionAlternative((LootItemCondition[]) this.terms.toArray(new LootItemCondition[0]));
        }
    }

    public static class b implements LootSerializer<LootItemConditionAlternative> {

        public b() {}

        public void serialize(JsonObject jsonobject, LootItemConditionAlternative lootitemconditionalternative, JsonSerializationContext jsonserializationcontext) {
            jsonobject.add("terms", jsonserializationcontext.serialize(lootitemconditionalternative.terms));
        }

        @Override
        public LootItemConditionAlternative deserialize(JsonObject jsonobject, JsonDeserializationContext jsondeserializationcontext) {
            LootItemCondition[] alootitemcondition = (LootItemCondition[]) ChatDeserializer.getAsObject(jsonobject, "terms", jsondeserializationcontext, LootItemCondition[].class);

            return new LootItemConditionAlternative(alootitemcondition);
        }
    }
}
