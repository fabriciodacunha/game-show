import * as dayjs from 'dayjs';
import { IQuestao } from 'app/entities/questao/questao.model';
import { IParticipante } from 'app/entities/participante/participante.model';
import { IPlay } from 'app/entities/play/play.model';
import { Alternativa } from 'app/entities/enumerations/alternativa.model';

export interface IResposta {
  id?: number;
  respostaData?: dayjs.Dayjs | null;
  respostaAlternativa?: Alternativa | null;
  respostaCerta?: boolean | null;
  questao?: IQuestao | null;
  participante?: IParticipante | null;
  play?: IPlay | null;
}

export class Resposta implements IResposta {
  constructor(
    public id?: number,
    public respostaData?: dayjs.Dayjs | null,
    public respostaAlternativa?: Alternativa | null,
    public respostaCerta?: boolean | null,
    public questao?: IQuestao | null,
    public participante?: IParticipante | null,
    public play?: IPlay | null
  ) {
    this.respostaCerta = this.respostaCerta ?? false;
  }
}

export function getRespostaIdentifier(resposta: IResposta): number | undefined {
  return resposta.id;
}
