"19208613": "    public Object clone ()\n    {\n        try\n        {\n            final IntVector _clone = (IntVector) super.clone ();\n            \n            // deep clone:\n            if (m_size < COPY_THRESHOLD)\n            {\n                _clone.m_values = new int [m_values.length];\n                final int [] _clone_values = _clone.m_values;\n                for (int i = 0; i < m_size; ++ i) _clone_values [i] = m_values [i];\n            }\n            else\n            {\n                _clone.m_values = (int []) m_values.clone ();\n            }\n            \n            return _clone;\n        }\n        catch (CloneNotSupportedException e)\n        {\n            throw new InternalError (e.toString ());\n        }\n",


19208613	    public Object clone ()
    {
        try
        {
            final IntVector _clone = (IntVector) super.clone ();
            
            // deep clone:
            if (m_size < COPY_THRESHOLD)
            {
                _clone.m_values = new int [m_values.length];
                final int [] _clone_values = _clone.m_values;
                for (int i = 0; i < m_size; ++ i) _clone_values [i] = m_values [i];
            }
            else
            {
                _clone.m_values = (int []) m_values.clone ();
            }
            
            return _clone;
        }
        catch (CloneNotSupportedException e)
        {
            throw new InternalError (e.toString ());
        }


com.github.javaparser.ParseProblemException: (line 43167,col 9) Parse error. Found "protected", expected one of  "catch" "finally" "}"


Problem: some methods are broken (this one for example does not have a closing bracket)