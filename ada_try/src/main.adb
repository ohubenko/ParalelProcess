with Ada.Text_IO;
with Ada.Text_IO; use Ada.Text_IO;
with Ada.Containers.Vectors;

procedure Main is
   Count_Wave : Integer := 1;
   Count_Number : Integer :=11;
   Size : Float := Float(Count_Number);
   Coun : Integer := 0;

   procedure Calculate (Data: Integer);

   procedure Calculate (Data: Integer) is

   package Data_Vectors is new Ada.Containers.Vectors (Index_Type => Natural, Element_Type => Integer);
   use Data_Vectors;
   Vector_Data : Data_Vectors.Vector;

   task type Task_Wave is
      entry Start;
      end Task_Wave;

   type Task_Ptr is access all Task_Wave;
   type Task_Array is array (1..Data) of Task_Ptr;

      Tasks : Task_Array;

   task body Task_Wave is
      function Sum (Num1, Num2 : Integer) return Integer is
   begin
         Put_Line (Integer'Image (Num1) & " + " & Integer'Image (Num2));
         return Num1 + Num2;
      end Sum;
      Result : Data_Vectors.Vector;
      sum1 : Integer;

      begin
         accept Start do

      Put_Line ("Pre-existing array: " & Data_Vectors.Vector'Image (Vector_Data));
      Put_Line ("Wave: " & Integer'Image (Count_Wave));
      Count_Wave := Count_Wave + 1;

         for I in 1 .. (Integer(Vector_Data.Length) + 1) / 2 loop
          if Integer(Vector_Data.Length) = 1 then
            Result.Append(Vector_Data.First_Element);
            Vector_Data.Delete_First;
          else
             sum1 := Sum(Vector_Data.First_Element, Vector_Data.Last_Element);
             Result.Append(sum1);
             Vector_Data.Delete_First;
             Vector_Data.Delete_Last;
            end if;
       end loop;
         Vector_Data := Result;
         Put_Line (Data_Vectors.Vector'Image (Vector_Data));
         Result.Clear;
         end Start;

      end Task_Wave;

   begin

    for I in 1 .. Count_Number loop
      Vector_Data.Append(I);
    end loop;

    for I in Tasks'Range loop
         Tasks(I) := new Task_Wave;
         Tasks(I).Start;
    end loop;

   end Calculate;

begin
   while Size >= 1.0 loop
      Size := Size / 2.0;
      Coun := Coun + 1;
   end loop;

   Calculate(Coun);

end Main;
