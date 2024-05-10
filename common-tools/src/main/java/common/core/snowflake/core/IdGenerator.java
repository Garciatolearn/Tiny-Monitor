package common.core.snowflake.core;
public interface IdGenerator {
   default long nextId(){
       return 0L;
   };

    default String nextIdStr(){
        return "";
    };
}
