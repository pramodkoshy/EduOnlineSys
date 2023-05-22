export interface IQuestionItem {
  id?: number;
  name?: string;
  number?: number;
  answerItemNumber?: string;
  answerItemId?: number;
  questionName?: string;
  questionId?: number;
}

export class QuestionItem implements IQuestionItem {
  constructor(
    public id?: number,
    public name?: string,
    public number?: number,
    public answerItemNumber?: string,
    public answerItemId?: number,
    public questionName?: string,
    public questionId?: number
  ) {}
}
