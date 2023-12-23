package me.ethan;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.ethan.moves.Forward;

import java.util.List;

@Data
@AllArgsConstructor
public class ExecuteBytecode {

    //testing holdupp
    public void execute(List<Byte> bytecode){
        for(Byte bit : bytecode){
            switch (bit) {
                case 10:
                    //forward.execute();
                case 11:
                    //attack.execute();
            }
        }
    }

}
