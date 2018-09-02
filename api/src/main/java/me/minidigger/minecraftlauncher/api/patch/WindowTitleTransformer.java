package me.minidigger.minecraftlauncher.api.patch;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.GeneratorAdapter;

public class WindowTitleTransformer extends ClassVisitor {

        private String value;
        private String replacement;

        public WindowTitleTransformer(ClassVisitor cv, String value, String replacement) {
            super(Opcodes.ASM6, cv);
            this.value = value;
            this.replacement = replacement;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);

            if (name.equalsIgnoreCase("<init>")) {
                return new GeneratorAdapter(Opcodes.ASM6, methodVisitor, access, name, descriptor) {

                    @Override
                    public void visitLdcInsn(Object cst) {
                        if (cst instanceof String) {
                            String curr = (String) cst;
                            if (curr.equals(value)) {
                                super.visitLdcInsn(replacement);
                                return;
                            }
                        }

                        super.visitLdcInsn(cst);
                    }
                };
            }else{
                return methodVisitor;
            }
        }
    }